package mx.uam.tsis.ejemplobackend.servicios.dto;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para la gestión de grupos")
public class GrupoDTO {
    
    @Schema(description = "ID del grupo")
    private Integer id;
    
    @NotBlank
    @Schema(description = "Clave del grupo", required = true)
    private String clave;
    
    @Schema(description = "Lista de alumnos en el grupo")
    private List<AlumnoDTO> alumnos = new ArrayList<>();
    
    public GrupoDTO() {
    }

    public GrupoDTO(Integer id, String clave, List<AlumnoDTO> alumnos) {
        this.id = id;
        this.clave = clave;
        this.alumnos = alumnos;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public List<AlumnoDTO> getAlumnos() {
        return alumnos;
    }

    public void setAlumnos(List<AlumnoDTO> alumnos) {
        this.alumnos = alumnos;
    }
    
    public boolean addAlumno(AlumnoDTO alumno) {
        return alumnos.add(alumno);
    }
} 