package com.backend.portalroshkabackend.Repositories.SYSADMIN;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.portalroshkabackend.Models.DispositivoAsignado;

@Repository
public interface InventarioAsignadoRepository extends JpaRepository<DispositivoAsignado, Integer> {
    
}
