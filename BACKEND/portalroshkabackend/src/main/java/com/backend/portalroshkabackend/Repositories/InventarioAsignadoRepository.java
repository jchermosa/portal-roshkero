package com.backend.portalroshkabackend.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.portalroshkabackend.Models.InventarioAsignado;

@Repository
public interface InventarioAsignadoRepository extends JpaRepository<InventarioAsignado, Integer> {
    
}
