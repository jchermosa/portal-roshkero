package com.backend.portalroshkabackend.Repositories.TH;

import com.backend.portalroshkabackend.Models.Cargos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CargosRepository extends JpaRepository<Cargos, Integer> {
    Optional<Cargos> findByNombre(String nombre);// for Vladimir

    Boolean existsByNombre(String nombre);

    Boolean existsByNombreAndIdCargoNot(String nombre, Integer idCargo);
}