package mx.uam.tsis.ejemplobackend.servicios.dto;

import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para la creación de grupos")
public class GrupoCreateDTO {
    
    @NotBlank
    @Schema(description = "Clave del grupo", required = true)
    private String clave;
    
    public GrupoCreateDTO() {
    }

    public GrupoCreateDTO(String clave) {
        this.clave = clave;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }
} 