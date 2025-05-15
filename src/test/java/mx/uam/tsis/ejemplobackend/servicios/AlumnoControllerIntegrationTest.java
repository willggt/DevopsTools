package mx.uam.tsis.ejemplobackend.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import mx.uam.tsis.ejemplobackend.datos.AlumnoRepository;
import mx.uam.tsis.ejemplobackend.modelo.Alumno;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

/**
 * 
 * Prueba de integración para el endpoint alumnos
 * 
 * @author humbertocervantes
 *
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AlumnoControllerIntegrationTest {
	
	private static final Logger log = LoggerFactory.getLogger(AlumnoControllerIntegrationTest.class);

	@Autowired
	private TestRestTemplate testRestTemplate;
	
	@Autowired
	private AlumnoRepository alumnoRepository;
	
	@BeforeEach
	public void prepare() {
		
		// Aqui se puede hacer cosas para preparar sus casos de prueba, incluyendo
		// agregar datos a la BD
		
	}
	
	@AfterEach
	public void cleanUp() {
		
		// Me aseguro de limpiar los cambios en la BD después de cada prueba
		alumnoRepository.deleteAll();
	}
	
	@Test
	public void testPost() {
		
		// Creo el alumno que voy a enviar (dto)
		Alumno alumnoDto = new Alumno();
		alumnoDto.setCarrera("Computación");
		alumnoDto.setMatricula(12345678);
		alumnoDto.setNombre("Pruebin");

		// Creo el encabezado
		HttpHeaders headers = new HttpHeaders();
		headers.set("content-type",MediaType.APPLICATION_JSON.toString());
		
		// Caso de prueba exitoso
		// Creo la petición con el alumno como body y el encabezado
		HttpEntity <Alumno> request = new HttpEntity <> (alumnoDto, headers);
		
		ResponseEntity <Alumno> responseEntity = 
				testRestTemplate.exchange("/v1/alumnos", HttpMethod.POST, request, Alumno.class);

		// Corroboro que el endpoint me regresa el estatus esperado
		assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
		assertTrue(responseEntity.hasBody(), "Response should have a body");
		assertEquals(alumnoDto.getMatricula(), responseEntity.getBody().getMatricula());
		
		// Corroboro que en la base de datos se guardó el alumno
		Optional <Alumno> optAlumno = alumnoRepository.findById(alumnoDto.getMatricula());
		assertEquals(alumnoDto,optAlumno.get());
	}
	
	@Test
	public void testPostDuplicate() {
		// Given - Create a student first
		Alumno alumno = new Alumno();
		alumno.setCarrera("Computación");
		alumno.setMatricula(12345678);
		alumno.setNombre("Pruebin");
		
		alumnoRepository.save(alumno);
		
		// Try to create the same student again
		HttpHeaders headers = new HttpHeaders();
		headers.set("content-type", MediaType.APPLICATION_JSON.toString());
		
		HttpEntity<Alumno> request = new HttpEntity<>(alumno, headers);
		
		// When - Attempt to create duplicate
		ResponseEntity<String> responseEntity = 
				testRestTemplate.exchange("/v1/alumnos", HttpMethod.POST, request, String.class);
		
		// Then - Expect error
		assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
		assertTrue(responseEntity.getBody().contains("Alumno") && responseEntity.getBody().contains("already exists"));
	}

	@Test
	public void testUpdate200() {
		
		Alumno alumno = new Alumno();
		alumno.setMatricula(12345678);
		alumno.setCarrera("Computación");
		alumno.setNombre("Pruebin");
		
		alumnoRepository.save(alumno); // Guardo el alumno original en la BD

		Alumno alumnoActualizado = new Alumno();
		alumnoActualizado.setMatricula(12345678);
		alumnoActualizado.setCarrera("Electrónica");
		alumnoActualizado.setNombre("PruebinActualizado");
		
		
		// Creo el encabezado
		HttpHeaders headers = new HttpHeaders();
		headers.set("content-type",MediaType.APPLICATION_JSON.toString());
		
		// Creo la petición con el alumno como body y el encabezado
		HttpEntity <Alumno> request = new HttpEntity <> (alumnoActualizado, headers);
		
		ResponseEntity <Alumno> responseEntity = testRestTemplate.exchange("/v1/alumnos/12345678", HttpMethod.PUT, request, Alumno.class);

		// Corroboro que el endpoint me regresa el estatus esperado
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertTrue(responseEntity.hasBody(), "Response should have a body");

		// Recupero de la BD el alumno
		Optional <Alumno> optAlumno = alumnoRepository.findById(12345678);
		
		Alumno actualizado = optAlumno.get();
		
		// Aquí corroboro que el alumno que está en la BD ya quedó actualizado
		assertEquals(alumnoActualizado, actualizado);
	}
	
	@Test
	public void testUpdateNotFound() {
		// Given - Student doesn't exist in DB
		Alumno alumnoActualizado = new Alumno();
		alumnoActualizado.setMatricula(12345678);
		alumnoActualizado.setCarrera("Electrónica");
		alumnoActualizado.setNombre("PruebinActualizado");
		
		// Create headers and request
		HttpHeaders headers = new HttpHeaders();
		headers.set("content-type", MediaType.APPLICATION_JSON.toString());
		
		HttpEntity<Alumno> request = new HttpEntity<>(alumnoActualizado, headers);
		
		// When - Try to update non-existent student
		ResponseEntity<String> responseEntity = testRestTemplate.exchange(
			"/v1/alumnos/12345678", 
			HttpMethod.PUT, 
			request, 
			String.class
		);
		
		// Then - Expect not found
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
		assertTrue(responseEntity.getBody().contains("Alumno") && responseEntity.getBody().contains("not found"));
	}
	
	@Test
	public void testGetByMatriculaNotFound() {
		// When - Try to get non-existent student
		ResponseEntity<String> responseEntity = testRestTemplate.exchange(
			"/v1/alumnos/12345678", 
			HttpMethod.GET, 
			null, 
			String.class
		);
		
		// Then - Expect not found
		assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
		assertTrue(responseEntity.getBody().contains("Alumno") && responseEntity.getBody().contains("not found"));
	}
}
