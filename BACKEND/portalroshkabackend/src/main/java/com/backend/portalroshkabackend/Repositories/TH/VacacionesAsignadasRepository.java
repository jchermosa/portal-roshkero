package com.backend.portalroshkabackend.Repositories.TH;

import com.backend.portalroshkabackend.Models.VacacionesAsignadas;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VacacionesAsignadasRepository extends JpaRepository<VacacionesAsignadas, Integer> {

    Optional<VacacionesAsignadas> findBySolicitud_idSolicitud(Integer idSolicitud);
}
