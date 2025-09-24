package com.backend.portalroshkabackend.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.portalroshkabackend.Models.TipoDispositivo;

@Repository
public interface DeviceTypesRepository extends JpaRepository<TipoDispositivo, Integer> {

    
}
