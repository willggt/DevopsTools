package mx.uam.tsis.ejemplobackend.servicios.dto.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import mx.uam.tsis.ejemplobackend.modelo.Grupo;
import mx.uam.tsis.ejemplobackend.servicios.dto.GrupoDTO;
import mx.uam.tsis.ejemplobackend.servicios.dto.AlumnoDTO;

public class GrupoMapper {
    
    private GrupoMapper() {
        // Private constructor to prevent instantiation
    }
    
    public static GrupoDTO toDTO(Grupo grupo) {
        if (grupo == null) {
            return null;
        }
        
        List<AlumnoDTO> alumnosDTO = grupo.getAlumnos().stream()
            .map(AlumnoMapper::toDTO)
            .collect(Collectors.toList());
            
        return new GrupoDTO(
            grupo.getId(),
            grupo.getClave(),
            alumnosDTO
        );
    }
    
    public static Grupo toEntity(GrupoDTO grupoDTO) {
        if (grupoDTO == null) {
            return null;
        }
        
        return new Grupo(
            grupoDTO.getId(),
            grupoDTO.getClave(),
            new ArrayList<>()
        );
    }
    
    public static List<GrupoDTO> toDTOList(List<Grupo> grupos) {
        if (grupos == null) {
            return new ArrayList<>();
        }
        
        return grupos.stream()
            .map(GrupoMapper::toDTO)
            .collect(Collectors.toList());
    }
} 