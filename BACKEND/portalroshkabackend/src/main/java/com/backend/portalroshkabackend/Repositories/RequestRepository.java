package com.backend.portalroshkabackend.Repositories;

import com.backend.portalroshkabackend.Models.Solicitudes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestRepository extends JpaRepository<Solicitudes, Integer> {
}
