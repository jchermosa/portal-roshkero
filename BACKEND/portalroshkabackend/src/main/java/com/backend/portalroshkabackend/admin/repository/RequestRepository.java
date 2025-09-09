package com.backend.portalroshkabackend.admin.repository;

import com.backend.portalroshkabackend.common.model.Solicitudes;
import com.backend.portalroshkabackend.common.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestRepository extends JpaRepository<Solicitudes, Integer> {
}
