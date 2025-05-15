package mx.uam.tsis.ejemplobackend.negocio;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import mx.uam.tsis.ejemplobackend.datos.GrupoRepository;
import mx.uam.tsis.ejemplobackend.modelo.Alumno;
import mx.uam.tsis.ejemplobackend.modelo.Grupo;
import mx.uam.tsis.ejemplobackend.negocio.exception.EntityNotFoundException;
import mx.uam.tsis.ejemplobackend.negocio.exception.DuplicateEntityException;

import java.util.ArrayList;
import java.util.Optional;

@ExtendWith(MockitoExtension.class) // Uso de Mockito
public class GrupoServiceTest {

	@Mock
	private GrupoRepository grupoRepositoryMock;
	
	@Mock
	private AlumnoService alumnoServiceMock;
	
	@InjectMocks
	private GrupoService grupoService;
	
	@Test
	public void testSuccessfulCreate() {
		// Given
		String clave = "TST01";
		Grupo grupo = new Grupo(null, clave, new ArrayList<>());
		Grupo grupoGuardado = new Grupo(1, clave, new ArrayList<>());
		
		// When
		when(grupoRepositoryMock.save(grupo)).thenReturn(grupoGuardado);
		
		// Then
		Grupo result = grupoService.create(clave);
		assertNotNull(result);
		assertEquals(clave, result.getClave());
		assertTrue(result.getAlumnos().isEmpty());
	}
	
	@Test
	public void testSuccesfulAddStudentToGroup() {
		// Given
		Grupo grupo = new Grupo(1, "TST01", new ArrayList<>());
		Alumno alumno = new Alumno();
		alumno.setCarrera("Computación");
		alumno.setMatricula(12345678);
		alumno.setNombre("Pruebin");
		
		// When
		when(alumnoServiceMock.findByMatricula(12345678)).thenReturn(alumno);
		when(grupoRepositoryMock.findById(1)).thenReturn(Optional.of(grupo));
		when(grupoRepositoryMock.save(grupo)).thenReturn(grupo); // Asegurarse que el mock de save es llamado
		
		// Then
		boolean result = grupoService.addStudentToGroup(1, 12345678);
		assertTrue(result);
		assertEquals(1, grupo.getAlumnos().size());
		assertEquals(alumno, grupo.getAlumnos().get(0));
		verify(grupoRepositoryMock).save(grupo); // Verificar que save fue llamado
	}
	
	@Test
	public void testUnsuccessfulAddStudentToGroup_GroupNotFound() {
		// Given
		Alumno alumno = new Alumno();
		alumno.setMatricula(12345678);
		
		// When
		when(grupoRepositoryMock.findById(anyInt())).thenReturn(Optional.empty());
		
		// Then
		EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
			grupoService.addStudentToGroup(1, 12345678);
		});
		assertEquals("Grupo", exception.getEntityType());
		assertEquals("1", exception.getIdentifier());
		verify(grupoRepositoryMock, never()).save(org.mockito.ArgumentMatchers.any(Grupo.class));
	}

	@Test
	public void testUnsuccessfulAddStudentToGroup_StudentNotFound() {
		// Given
		Grupo grupo = new Grupo(1, "TST01", new ArrayList<>());

		// When
		when(grupoRepositoryMock.findById(1)).thenReturn(Optional.of(grupo));
		when(alumnoServiceMock.findByMatricula(12345678)).thenThrow(new EntityNotFoundException("Alumno", "12345678"));

		// Then
		EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
			grupoService.addStudentToGroup(1, 12345678);
		});
		assertEquals("Alumno", exception.getEntityType());
		assertEquals("12345678", exception.getIdentifier());
		verify(grupoRepositoryMock, never()).save(org.mockito.ArgumentMatchers.any(Grupo.class));
	}

	@Test
	public void testUnsuccessfulAddDuplicateStudentToGroup() {
		// Given
		Grupo grupo = new Grupo(1, "TST01", new ArrayList<>());
		Alumno alumno = new Alumno();
		alumno.setMatricula(12345678);
		
		// Agregamos el alumno por primera vez
		grupo.addAlumno(alumno); // Esto simula que el alumno ya está en la lista interna del grupo
		
		// When
		when(alumnoServiceMock.findByMatricula(12345678)).thenReturn(alumno);
		when(grupoRepositoryMock.findById(1)).thenReturn(Optional.of(grupo));
		
		// Then
		DuplicateEntityException exception = assertThrows(DuplicateEntityException.class, () -> {
			grupoService.addStudentToGroup(1, 12345678);
		});
		assertEquals("Alumno", exception.getEntityType());
		assertEquals("12345678", exception.getIdentifier());
		assertEquals("Grupo 1", exception.getContext());
		assertEquals(1, grupo.getAlumnos().size(), "El grupo debe mantener solo un alumno");
		verify(grupoRepositoryMock, never()).save(grupo); // Save no debe ser llamado si el alumno ya existe
	}
}
