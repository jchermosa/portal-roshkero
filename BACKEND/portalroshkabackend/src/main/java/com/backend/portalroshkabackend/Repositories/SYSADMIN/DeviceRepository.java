package com.backend.portalroshkabackend.Repositories.SYSADMIN;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.backend.portalroshkabackend.Models.Dispositivo;

@Repository
public interface DeviceRepository extends JpaRepository<Dispositivo, Integer> {

    @Query("SELECT d FROM Dispositivo d WHERE d.encargado IS NULL AND d.estado = 'D'")
    List<Dispositivo> findAllWithoutOwner();

    @Query("SELECT d FROM Dispositivo d WHERE d.ubicacion.idUbicacion = :ubicacionId")
    List<Dispositivo> findAllInLocation(@Param("ubicacionId") Integer ubicacionId);

    @Query("SELECT d FROM Dispositivo d WHERE d.ubicacion.idUbicacion = :ubicacionId")
    Page<Dispositivo> findAllInLocation(@Param("ubicacionId") Integer ubicacionId, Pageable pageable);

    @Query("SELECT d FROM Dispositivo d WHERE d.encargado IS NULL AND d.estado = 'D'")
   Page<Dispositivo> findAllWithoutOwner(Pageable pageable);

    @Query("SELECT d FROM Dispositivo d WHERE d.encargado IS NULL AND d.tipoDispositivo.idTipoDispositivo = :tipoDispositivoId and d.estado = 'D'")
    Page<Dispositivo> findAllByTipoDispositivo_IdTipoDispositivoAndEncargadoIsNull(@Param("tipoDispositivoId") Integer tipoDispositivoId, Pageable pageable);
} 
