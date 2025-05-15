package mx.uam.tsis.ejemplobackend.datos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mx.uam.tsis.ejemplobackend.modelo.Alumno;

/**
 * Se encarga de almacenar y recuperar alumnos
 * 
 * @author humbertocervantes
 *
 */
@Repository
public interface AlumnoRepository extends JpaRepository<Alumno, Integer> {
}
