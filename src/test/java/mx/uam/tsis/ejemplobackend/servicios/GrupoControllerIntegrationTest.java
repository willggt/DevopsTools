package mx.uam.tsis.ejemplobackend.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import mx.uam.tsis.ejemplobackend.datos.AlumnoRepository;
import mx.uam.tsis.ejemplobackend.datos.GrupoRepository;
import mx.uam.tsis.ejemplobackend.modelo.Alumno;
import mx.uam.tsis.ejemplobackend.modelo.Grupo;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class GrupoControllerIntegrationTest {
	
	private static final Logger log = LoggerFactory.getLogger(GrupoControllerIntegrationTest.class);
	
	@Autowired
	private TestRestTemplate testRestTemplate;
	
	@Autowired
	private GrupoRepository grupoRepository;
	
	@Autowired
	private AlumnoRepository alumnoRepository;
	
	@BeforeEach
	public void prepare() {
		// Limpiamos la base de datos antes de cada prueba
		grupoRepository.deleteAll();
		alumnoRepository.deleteAll();
	}
	
	@AfterEach
	public void cleanUp() {
		// Limpiamos la base de datos después de cada prueba
		grupoRepository.deleteAll();
		alumnoRepository.deleteAll();
	}
	
	@Test
	public void testAddStudentToGroup() {
		// Given
		// Creamos un grupo
		Grupo grupo = new Grupo();
		grupo.setClave("TST01");
		grupo = grupoRepository.save(grupo);

		// Creamos un alumno
		Alumno alumno = new Alumno();
		alumno.setMatricula(12345678);
		alumno.setNombre("Pruebin");
		alumno.setCarrera("Computación");
		alumno = alumnoRepository.save(alumno);
		alumnoRepository.flush();

		log.info("Testing testAddStudentToGroup with groupId: {} and matricula: {}", grupo.getId(), alumno.getMatricula());
		
		// When
		// Intentamos agregar el alumno al grupo
		ResponseEntity<String> responseEntity = testRestTemplate.exchange(
			"/v1/grupos/" + grupo.getId() + "/alumnos?matricula=" + alumno.getMatricula(),
			HttpMethod.POST,
			null,
			String.class
		);
		
		// Then
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		
		// Verificamos que el alumno se agregó al grupo
		Optional<Grupo> grupoOpt = grupoRepository.findById(grupo.getId());
		assertTrue(grupoOpt.isPresent());
		Grupo grupoActualizado = grupoOpt.get();
		assertEquals(1, grupoActualizado.getAlumnos().size());
		assertEquals(alumno.getMatricula(), grupoActualizado.getAlumnos().get(0).getMatricula());
	}
	
	@Test
	public void testAddDuplicateStudentToGroup() {
		// Given
		// Creamos un grupo
		Grupo grupo = new Grupo();
		grupo.setClave("TST01");
		grupo = grupoRepository.save(grupo);
		
		// Creamos un alumno
		Alumno alumno = new Alumno();
		alumno.setMatricula(12345678);
		alumno.setNombre("Pruebin");
		alumno.setCarrera("Computación");
		alumno = alumnoRepository.save(alumno);
		alumnoRepository.flush();
		
		// Agregamos el alumno al grupo una primera vez
		grupo.addAlumno(alumno);
		grupoRepository.save(grupo);

		log.info("Testing testAddDuplicateStudentToGroup with groupId: {} and matricula: {}", grupo.getId(), alumno.getMatricula());
		
		// When
		// Intentamos agregar el mismo alumno al grupo
		ResponseEntity<String> responseEntity = testRestTemplate.exchange(
			"/v1/grupos/" + grupo.getId() + "/alumnos?matricula=" + alumno.getMatricula(),
			HttpMethod.POST,
			null,
			String.class
		);
		
		// Then
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		assertTrue(responseEntity.getBody().contains("Alumno") && responseEntity.getBody().contains("already exists"));
		
		// Verificamos que el grupo sigue teniendo solo un alumno
		Optional<Grupo> grupoOpt = grupoRepository.findById(grupo.getId());
		assertTrue(grupoOpt.isPresent());
		Grupo grupoActualizado = grupoOpt.get();
		assertEquals(1, grupoActualizado.getAlumnos().size());
	}
	
	@Test
	public void testAddStudentToNonExistentGroup() {
		// Given
		// Creamos un alumno
		Alumno alumno = new Alumno();
		alumno.setMatricula(12345678);
		alumno.setNombre("Pruebin");
		alumno.setCarrera("Computación");
		alumno = alumnoRepository.save(alumno);
		
		// When
		// Intentamos agregar el alumno a un grupo que no existe
		ResponseEntity<String> responseEntity = testRestTemplate.exchange(
			"/v1/grupos/999/alumnos?matricula=" + alumno.getMatricula(),
			HttpMethod.POST,
			null,
			String.class
		);
		
		// Then
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
		assertTrue(responseEntity.getBody().contains("Grupo") && responseEntity.getBody().contains("not found"));
	}
} 