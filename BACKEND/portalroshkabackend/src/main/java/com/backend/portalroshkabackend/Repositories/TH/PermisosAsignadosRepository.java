package com.backend.portalroshkabackend.Repositories.TH;

import com.backend.portalroshkabackend.Models.PermisosAsignados;
import com.backend.portalroshkabackend.Models.VacacionesAsignadas;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PermisosAsignadosRepository extends JpaRepository<PermisosAsignados, Integer> {

    Optional<PermisosAsignados> findBySolicitud_idSolicitud(Integer idSolicitud);
}
