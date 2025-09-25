package com.backend.portalroshkabackend.Repositories.SYSADMIN;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.backend.portalroshkabackend.Models.Solicitud;
import com.backend.portalroshkabackend.Models.Usuario;

@Repository
public interface SysAdminRepository extends JpaRepository<Usuario, Integer>{

    @Query(value = "SELECT * FROM solicitudes WHERE estado = 'P' AND tipo_solicitud = 'DISPOSITIVO'", nativeQuery = true)
    List<Solicitud> findSolicitudesPendientes(); 

    @Query(value = "SELECT * FROM solicitudes WHERE estado = 'A' AND tipo_solicitud = 'DISPOSITIVO'", nativeQuery = true)
    List<Solicitud> findSolicitudesAprobadas(); 

    @Query(value = "SELECT * FROM solicitudes WHERE estado = 'R' AND tipo_solicitud = 'DISPOSITIVO'", nativeQuery = true)
    List<Solicitud> findSolicitudesRechazadas();

    @Query(value = "SELECT * FROM solicitudes WHERE tipo_solicitud = 'DISPOSITIVO'", nativeQuery = true)
    List<Solicitud> findAllSolicitudes(); 
} 
    
