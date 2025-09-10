package com.backend.portalroshkabackend.admin.repository;

import com.backend.portalroshkabackend.common.model.Solicitudes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestRepository extends JpaRepository<Solicitudes, Integer> {
}
