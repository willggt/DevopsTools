package mx.uam.tsis.ejemplobackend.negocio;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.uam.tsis.ejemplobackend.datos.GrupoRepository;
import mx.uam.tsis.ejemplobackend.modelo.Alumno;
import mx.uam.tsis.ejemplobackend.modelo.Grupo;
import mx.uam.tsis.ejemplobackend.negocio.exception.EntityNotFoundException;
import mx.uam.tsis.ejemplobackend.negocio.exception.DuplicateEntityException;

@Service
public class GrupoService {
	
	private static final Logger log = LoggerFactory.getLogger(GrupoService.class);

	@Autowired
	private GrupoRepository grupoRepository;
	
	@Autowired
	private AlumnoService alumnoService;
	
	public Grupo create(String clave) {
		Grupo grupo = new Grupo(null, clave, new ArrayList<>());
		return grupoRepository.save(grupo);
	}
	
	public List<Grupo> retrieveAll() {
		return StreamSupport.stream(grupoRepository.findAll().spliterator(), false)
			.collect(Collectors.toList());
	}
	
	/**
	 * Busca un grupo por su ID
	 * @param id el ID del grupo
	 * @return Optional con el grupo si existe, vacío si no
	 */
	public Optional<Grupo> findById(Integer id) {
		return grupoRepository.findById(id);
	}
	
	/**
	 * 
	 * Método que permite agregar un alumno a un grupo
	 * 
	 * @param groupId el id del grupo
	 * @param matricula la matricula del alumno
	 * @return true si se agregó correctamente
	 * @throws EntityNotFoundException si el grupo o el alumno no existen
	 * @throws DuplicateEntityException si el alumno ya está en el grupo
	 */
	public boolean addStudentToGroup(Integer groupId, Integer matricula) {
		
		log.info("Attempting to add student with matricula {} to group with ID {}", matricula, groupId);
		
		// 1.- Recuperamos el grupo
		Optional<Grupo> grupoOpt = grupoRepository.findById(groupId);

		if (grupoOpt.isPresent()) {
			log.info("Group found in service: ID = {}", grupoOpt.get().getId());
		} else {
			log.warn("Group NOT found in service for ID: {}", groupId);
		}
		
		// 2.- Verificamos que el grupo exista
		if(!grupoOpt.isPresent()) {
			log.info("No se encontró el grupo");
			throw new EntityNotFoundException("Grupo", groupId.toString());
		}

		// 3.- Recuperamos primero al alumno
		Alumno alumno = alumnoService.findByMatricula(matricula);
		if (alumno == null) {
			log.info("No se encontró el alumno");
			throw new EntityNotFoundException("Alumno", matricula.toString());
		}
		
		// 4.- Agrego el alumno al grupo
		Grupo grupo = grupoOpt.get();
		boolean alumnoAgregado = grupo.addAlumno(alumno);
		
		// 5.- Solo persistimos si el alumno fue agregado exitosamente
		if (alumnoAgregado) {
			grupoRepository.save(grupo);
			log.info("Alumno agregado exitosamente al grupo");
		} else {
			log.info("El alumno ya existe en el grupo");
			throw new DuplicateEntityException("Alumno", matricula.toString(), "Grupo " + groupId);
		}
		
		return alumnoAgregado;
	}
}
