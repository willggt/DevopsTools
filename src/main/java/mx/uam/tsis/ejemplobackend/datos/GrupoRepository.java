package mx.uam.tsis.ejemplobackend.datos;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import mx.uam.tsis.ejemplobackend.modelo.Grupo;

@Repository
public interface GrupoRepository extends CrudRepository<Grupo, Integer> {
}
