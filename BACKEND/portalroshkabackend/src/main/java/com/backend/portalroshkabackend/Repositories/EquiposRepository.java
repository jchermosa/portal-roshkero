package com.backend.portalroshkabackend.Repositories;

import com.backend.portalroshkabackend.Models.Equipos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EquiposRepository extends JpaRepository<Equipos, Integer> {
    // Получить команду с клиентом по ID
    @Query("SELECT e FROM Equipos e LEFT JOIN FETCH e.cliente WHERE e.idEquipo = :id")
    Optional<Equipos> findByIdWithCliente(@Param("id") int id);

}
