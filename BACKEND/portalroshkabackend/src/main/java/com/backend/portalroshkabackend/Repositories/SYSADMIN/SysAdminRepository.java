package com.backend.portalroshkabackend.Repositories.SYSADMIN;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.backend.portalroshkabackend.Models.Solicitud;
import com.backend.portalroshkabackend.Models.Usuario;

@Repository
public interface SysAdminRepository extends JpaRepository<Usuario, Integer>{

    @Query(value = "SELECT * FROM solicitudes WHERE estado = 'P' AND tipo_solicitud = 'DISPOSITIVO'", nativeQuery = true)
    Page<Solicitud> findSolicitudesPendientes(Pageable pageable);

    @Query(value = "SELECT * FROM solicitudes WHERE estado = 'A' AND tipo_solicitud = 'DISPOSITIVO'", nativeQuery = true)
    Page<Solicitud> findSolicitudesAprobadas(Pageable pageable);

    @Query(value = "SELECT * FROM solicitudes WHERE estado = 'R' AND tipo_solicitud = 'DISPOSITIVO'", nativeQuery = true)
    Page<Solicitud> findSolicitudesRechazadas(Pageable pageable);

    @Query(value = "SELECT * FROM solicitudes WHERE tipo_solicitud = 'DISPOSITIVO'", nativeQuery = true)
    Page<Solicitud> findAllSolicitudes(Pageable pageable);
} 
    
