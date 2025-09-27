package com.backend.portalroshkabackend.Repositories.SYSADMIN;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.backend.portalroshkabackend.Models.Dispositivo;
import com.backend.portalroshkabackend.Models.TipoDispositivo;

@Repository
public interface DeviceRepository extends JpaRepository<Dispositivo, Integer> {

   @Query("SELECT d FROM Dispositivo d WHERE d.encargado IS NULL AND d.estado = 'D'") 
   List<Dispositivo> findAllWithoutOwner();

   List<Dispositivo> findAllByTipoDispositivo_IdTipoDispositivo(Integer id);
} 
