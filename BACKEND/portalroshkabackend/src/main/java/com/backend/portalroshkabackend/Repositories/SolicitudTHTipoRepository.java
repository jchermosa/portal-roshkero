package com.backend.portalroshkabackend.Repositories;

import com.backend.portalroshkabackend.Models.Solicitud;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SolicitudTHTipoRepository extends JpaRepository<Solicitud, Integer> {
}
