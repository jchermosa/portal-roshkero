package com.backend.portalroshkabackend.Repositories.TH;

import com.backend.portalroshkabackend.Models.BeneficiosAsignados;
import com.backend.portalroshkabackend.Models.PermisosAsignados;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BeneficiosAsignadosRepository extends JpaRepository<BeneficiosAsignados, Integer> {
}
