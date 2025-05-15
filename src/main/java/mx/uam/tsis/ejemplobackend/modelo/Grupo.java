package mx.uam.tsis.ejemplobackend.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;

@Entity // Indica que hay que persistir en BD
public class Grupo {
	
	@Id
	@GeneratedValue // Autogenera un ID único
	private Integer id;
	
	@NotBlank
	private String clave;
	
	@OneToMany(targetEntity = Alumno.class, fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
	@JoinColumn(name = "id") // No crea tabla intermedia	
	private List <Alumno> alumnos = new ArrayList <> ();
	
	public Grupo() {
	}

	public Grupo(Integer id, String clave, List<Alumno> alumnos) {
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

	public List<Alumno> getAlumnos() {
		return alumnos;
	}

	public void setAlumnos(List<Alumno> alumnos) {
		this.alumnos = alumnos;
	}
	
	/**
	 * Agrega un alumno al grupo si no existe previamente
	 * @param alumno El alumno a agregar
	 * @return true si el alumno fue agregado exitosamente, false si el alumno ya existe en el grupo
	 */
	public boolean addAlumno(Alumno alumno) {
		if (alumno == null) {
			return false;
		}
		
		// Verifica si el alumno ya existe en el grupo
		boolean alumnoExiste = alumnos.stream()
			.anyMatch(a -> a.getMatricula().equals(alumno.getMatricula()));
			
		if (alumnoExiste) {
			return false;
		}
		
		return alumnos.add(alumno);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Grupo other = (Grupo) obj;
		return Objects.equals(id, other.id);
	}
}
