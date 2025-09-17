package com.backend.portalroshkabackend.Repositories;

import com.backend.portalroshkabackend.Models.Equipos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquiposRepository extends JpaRepository<Equipos, Integer> {
    
    boolean existsByNombre(String nombre);//для обработки повторяющизся имен.
    List<Equipos> findAllByNombre(String nombre);
    Optional<Equipos> findByNombre(String nombre);
    boolean existsByNombreAndIdEquipoNot(String nuevoNombre, Integer idEquipo);

}
