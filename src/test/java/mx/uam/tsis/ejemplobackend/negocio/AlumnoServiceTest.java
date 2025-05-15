package mx.uam.tsis.ejemplobackend.negocio;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;

import mx.uam.tsis.ejemplobackend.datos.AlumnoRepository;
import mx.uam.tsis.ejemplobackend.modelo.Alumno;
import mx.uam.tsis.ejemplobackend.negocio.exception.DuplicateEntityException;
import mx.uam.tsis.ejemplobackend.negocio.exception.EntityNotFoundException;

import java.util.Optional;

@ExtendWith(MockitoExtension.class) // Uso de Mockito
public class AlumnoServiceTest {
	
	@Mock
	private AlumnoRepository alumnoRepositoryMock; // Mock generado por Mockito
	
	@InjectMocks
	private AlumnoService alumnoService; // La unidad a probar
	
	@Test
	public void testSuccessfulCreate() {
		// Given
		Alumno alumno = new Alumno();
		alumno.setCarrera("Computación");
		alumno.setMatricula(12345678);
		alumno.setNombre("Pruebin");
		
		// When
		when(alumnoRepositoryMock.findById(12345678)).thenReturn(Optional.empty());
		when(alumnoRepositoryMock.save(alumno)).thenReturn(alumno);
		
		// Then
		Alumno result = alumnoService.create(alumno);
		assertNotNull(result);
		assertEquals("Computación", result.getCarrera());
		assertEquals(12345678, result.getMatricula());
		assertEquals("Pruebin", result.getNombre());
	}
	
	@Test
	public void testUnsuccesfulCreate() {
		// Given
		Alumno alumno = new Alumno();
		alumno.setCarrera("Computación");
		alumno.setMatricula(12345678);
		alumno.setNombre("Pruebin");
		
		// When
		when(alumnoRepositoryMock.findById(12345678)).thenReturn(Optional.of(alumno));
		
		// Then
		DuplicateEntityException exception = assertThrows(DuplicateEntityException.class, () -> {
			alumnoService.create(alumno);
		});
		assertEquals("Alumno", exception.getEntityType());
		assertEquals("12345678", exception.getIdentifier());
	}
	
	@Test
	public void testSuccessfulUpdate() {
		// Given
		Alumno alumno = new Alumno();
		alumno.setCarrera("Computación");
		alumno.setMatricula(12345678);
		alumno.setNombre("Pruebin");
		
		Alumno alumnoActualizado = new Alumno();
		alumnoActualizado.setCarrera("Electrónica");
		alumnoActualizado.setMatricula(12345678);
		alumnoActualizado.setNombre("PruebinActualizado");
		
		// When
		when(alumnoRepositoryMock.findById(12345678)).thenReturn(Optional.of(alumno));
		when(alumnoRepositoryMock.save(alumnoActualizado)).thenReturn(alumnoActualizado);
		
		// Then
		Alumno result = alumnoService.update(alumnoActualizado);
		assertNotNull(result);
		assertEquals("Electrónica", result.getCarrera());
		assertEquals("PruebinActualizado", result.getNombre());
	}
	
	@Test
	public void testUnsuccessfulUpdate() {
		// Given
		Alumno alumnoActualizado = new Alumno();
		alumnoActualizado.setCarrera("Electrónica");
		alumnoActualizado.setMatricula(12345678);
		alumnoActualizado.setNombre("PruebinActualizado");
		
		// When
		when(alumnoRepositoryMock.findById(12345678)).thenReturn(Optional.empty());
		
		// Then
		EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
			alumnoService.update(alumnoActualizado);
		});
		assertEquals("Alumno", exception.getEntityType());
		assertEquals("12345678", exception.getIdentifier());
	}
	
	@Test
	public void testFindByMatriculaFound() {
		// Given
		Alumno alumno = new Alumno();
		alumno.setCarrera("Computación");
		alumno.setMatricula(12345678);
		alumno.setNombre("Pruebin");
		
		// When
		when(alumnoRepositoryMock.findById(12345678)).thenReturn(Optional.of(alumno));
		
		// Then
		Alumno result = alumnoService.findByMatricula(12345678);
		assertNotNull(result);
		assertEquals("Computación", result.getCarrera());
		assertEquals(12345678, result.getMatricula());
		assertEquals("Pruebin", result.getNombre());
	}
	
	@Test
	public void testFindByMatriculaNotFound() {
		// Given
		
		// When
		when(alumnoRepositoryMock.findById(12345678)).thenReturn(Optional.empty());
		
		// Then
		EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
			alumnoService.findByMatricula(12345678);
		});
		assertEquals("Alumno", exception.getEntityType());
		assertEquals("12345678", exception.getIdentifier());
	}
}
