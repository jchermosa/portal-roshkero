package com.backend.portalroshkabackend.Repositories;

import com.backend.portalroshkabackend.Models.Equipos;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquiposRepository extends JpaRepository<Equipos, Integer> {

    Optional<Equipos> findByCliente_IdCliente(int idCliente);

}
