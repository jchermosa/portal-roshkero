package com.backend.portalroshkabackend.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.backend.portalroshkabackend.Models.SolicitudDispositivos;
import com.backend.portalroshkabackend.Models.Usuario;

@Repository
public interface SysAdminRepository extends JpaRepository<Usuario, Integer>{

    @Query(value = "SELECT * FROM solicitud_dispositivos WHERE estado = 'P'", nativeQuery = true)
    List<SolicitudDispositivos> findSolicitudesPendientes(); 

    @Query(value = "SELECT * FROM solicitud_dispositivos WHERE estado = 'A'", nativeQuery = true)
    List<SolicitudDispositivos> findSolicitudesAprobadas(); 

    @Query(value = "SELECT * FROM solicitud_dispositivos WHERE estado = 'R'", nativeQuery = true)
    List<SolicitudDispositivos> findSolicitudesRechazadas();

    @Query(value = "SELECT * FROM solicitud_dispositivos", nativeQuery = true)
    List<SolicitudDispositivos> findAllSolicitudes(); 
} 
    
