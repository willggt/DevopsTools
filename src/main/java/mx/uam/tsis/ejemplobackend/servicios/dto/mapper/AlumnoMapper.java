package mx.uam.tsis.ejemplobackend.servicios.dto.mapper;

import mx.uam.tsis.ejemplobackend.modelo.Alumno;
import mx.uam.tsis.ejemplobackend.servicios.dto.AlumnoDTO;

public class AlumnoMapper {
    
    private AlumnoMapper() {
        // Private constructor to prevent instantiation
    }
    
    public static AlumnoDTO toDTO(Alumno alumno) {
        if (alumno == null) {
            return null;
        }
        
        return new AlumnoDTO(
            alumno.getMatricula(),
            alumno.getNombre(),
            alumno.getCarrera()
        );
    }
    
    public static Alumno toEntity(AlumnoDTO alumnoDTO) {
        if (alumnoDTO == null) {
            return null;
        }
        
        return new Alumno(
            alumnoDTO.getMatricula(),
            alumnoDTO.getNombre(),
            alumnoDTO.getCarrera()
        );
    }
} 