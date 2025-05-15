package mx.uam.tsis.ejemplobackend.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 
 * La clase que representa los alumno
 * 
 * @author humbertocervantes
 *
 */
@Entity // Indica que hay que persistir en BD
public class Alumno {
	
	@NotNull
	@Schema(description = "Matricula del alumno", required = true)
	@Id // Indica que este es llave primaria
	private Integer matricula;
	
	@NotBlank
	@Schema(description = "Nombre del alumno", required = true)
	private String nombre;

	@NotBlank
	@Schema(description = "Carrera del alumno", required = true)
	private String carrera;

	public Alumno() {
	}

	public Alumno(Integer matricula, String nombre, String carrera) {
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((matricula == null) ? 0 : matricula.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Alumno other = (Alumno) obj;
		if (matricula == null) {
			if (other.matricula != null)
				return false;
		} else if (!matricula.equals(other.matricula))
			return false;
		return true;
	}
}
