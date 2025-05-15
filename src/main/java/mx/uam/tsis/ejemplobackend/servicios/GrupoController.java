package mx.uam.tsis.ejemplobackend.servicios;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import mx.uam.tsis.ejemplobackend.negocio.GrupoService;
import mx.uam.tsis.ejemplobackend.modelo.Grupo;
import mx.uam.tsis.ejemplobackend.servicios.dto.GrupoCreateDTO;
import mx.uam.tsis.ejemplobackend.servicios.dto.GrupoDTO;
import mx.uam.tsis.ejemplobackend.servicios.dto.mapper.GrupoMapper;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Grupo", description = "API para gestionar grupos")
@RestController
@RequestMapping("/v1") // Versionamiento
public class GrupoController {
	
	private static final Logger log = LoggerFactory.getLogger(GrupoController.class);

	@Autowired
	private GrupoService grupoService;
	
	
	@Operation(
			summary = "Crear grupo",
			description = "Permite crear un nuevo grupo. Los alumnos deben ser agregados posteriormente usando el endpoint de agregar alumno a grupo."
			) // Documentacion del api
	@PostMapping(path = "/grupos", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity <?> create(@RequestBody @Valid GrupoCreateDTO nuevoGrupoDTO) { // Validaciones
				
		log.info("Recibí llamada a create con "+nuevoGrupoDTO); // Logging
		
		Grupo grupoCreado = grupoService.create(nuevoGrupoDTO.getClave());
		
		if(grupoCreado != null) {
			return ResponseEntity.status(HttpStatus.CREATED).body(GrupoMapper.toDTO(grupoCreado));			
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("no se puede crear grupo");
		}
	}
	
	@Operation(summary = "Obtener todos los grupos")
	@GetMapping(path = "/grupos", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity <?> retrieveAll() {
		
		List<Grupo> grupos = grupoService.retrieveAll();
		List<GrupoDTO> gruposDTO = grupos.stream()
			.map(GrupoMapper::toDTO)
			.collect(Collectors.toList());
		return ResponseEntity.status(HttpStatus.OK).body(gruposDTO); 
	}
	
	/**
	 * 
	 * POST /grupos/{id}/alumnos?matricula=1234
	 * 
	 * @return ResponseEntity con el estado de la operación
	 */
	@Operation(summary = "Agregar alumno a grupo")
	@PostMapping(path = "/grupos/{id}/alumnos", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity <?> addStudentToGroup(
			@Parameter(description = "ID del grupo") @PathVariable("id") Integer id,
			@Parameter(description = "Matrícula del alumno") @RequestParam("matricula") Integer matricula) {
		
		boolean result = grupoService.addStudentToGroup(id, matricula);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
