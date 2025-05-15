package mx.uam.tsis.ejemplobackend.negocio;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.uam.tsis.ejemplobackend.datos.AlumnoRepository;
import mx.uam.tsis.ejemplobackend.modelo.Alumno;
import mx.uam.tsis.ejemplobackend.negocio.exception.DuplicateEntityException;
import mx.uam.tsis.ejemplobackend.negocio.exception.EntityNotFoundException;

/**
 * 
 * Clase que contiene la lógica de negocio del manejo de alumnos
 * 
 * @author humbertocervantes
 *
 */
@Service
public class AlumnoService {

	private static final Logger log = LoggerFactory.getLogger(AlumnoService.class);

	@Autowired
	private AlumnoRepository alumnoRepository;
	
	/**
	 * 
	 * Método que permite crear nuevos alumnos
	 * 
	 * @param alumno el alumno que se desea crear en el sistema
	 * @return el alumno que se acaba de crear si la creación es exitosa
	 * @throws DuplicateEntityException si ya existe un alumno con la misma matrícula
	 */
	public Alumno create(Alumno alumno) {
		// Regla de negocio: No se puede crear más de un alumno con la misma matricula
		Optional<Alumno> alumnoOpt = alumnoRepository.findById(alumno.getMatricula());
		
		if(!alumnoOpt.isPresent()) {
			log.info("Creado alumno con matricula "+alumno.getMatricula()); // Logging
			return alumnoRepository.save(alumno);
		} else {
			throw new DuplicateEntityException("Alumno", alumno.getMatricula().toString(), "sistema");
		}
	}
	
	/**
	 * Recupera todos los alumnos del sistema
	 * 
	 * @return lista de alumnos
	 */
	public List<Alumno> retrieveAll() {
		List<Alumno> result = new ArrayList<>();
		
		for(Alumno alumno:alumnoRepository.findAll()) {
			result.add(alumno);
		}
				
		return result;
	}
	
	/**
	 * Busca un alumno por su matrícula
	 * 
	 * @param matricula la matrícula del alumno
	 * @return el alumno encontrado
	 * @throws EntityNotFoundException si no se encuentra un alumno con esa matrícula
	 */
	public Alumno findByMatricula(Integer matricula) {
		Optional<Alumno> alumnoOpt = alumnoRepository.findById(matricula);
		if (alumnoOpt.isPresent()) {
			return alumnoOpt.get();
		} else {
			throw new EntityNotFoundException("Alumno", matricula.toString());
		}
	}
	
	/**
	 * 
	 * Actualiza al alumno
	 * 
	 * @param alumno el alumno actualizado
	 * @return el alumno actualizado
	 * @throws EntityNotFoundException si no se encuentra un alumno con esa matrícula
	 */
	public Alumno update(Alumno alumno) {
		Optional<Alumno> alumnoOpt = alumnoRepository.findById(alumno.getMatricula());
		
		if(alumnoOpt.isPresent()) {
			return alumnoRepository.save(alumno);
		} else {
			throw new EntityNotFoundException("Alumno", alumno.getMatricula().toString());
		}
	}
}
