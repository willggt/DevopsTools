package mx.uam.tsis.ejemplobackend.servicios.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para la gestión de alumnos")
public class AlumnoDTO {
    
    @NotNull
    @Schema(description = "Matricula del alumno", required = true)
    private Integer matricula;
    
    @NotBlank
    @Schema(description = "Nombre del alumno", required = true)
    private String nombre;

    @NotBlank
    @Schema(description = "Carrera del alumno", required = true)
    private String carrera;

    public AlumnoDTO() {
    }

    public AlumnoDTO(Integer matricula, String nombre, String carrera) {
        this.matricula = matricula;
        this.nombre = nombre;
        this.carrera = carrera;
    }

    public Integer getMatricula() {
        return matricula;
    }

    public void setMatricula(Integer matricula) {
        this.matricula = matricula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }
} 