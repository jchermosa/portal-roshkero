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
    
    // ✅ CORRECTO - Usar 'idCliente' en lugar de 'cliente'
    @Query("SELECT e FROM Equipos e LEFT JOIN FETCH e.idCliente WHERE e.idEquipo = :id")
    Optional<Equipos> findByIdWithCliente(@Param("id") int id);
    
    // Métodos adicionales que podrías necesitar
    List<Equipos> findByEstado(com.backend.portalroshkabackend.Models.Enum.EstadoActivoInactivo estado);
    
    @Query("SELECT e FROM Equipos e WHERE e.idCliente.idCliente = :clienteId")
    List<Equipos> findByClienteId(@Param("clienteId") Integer clienteId);
}
