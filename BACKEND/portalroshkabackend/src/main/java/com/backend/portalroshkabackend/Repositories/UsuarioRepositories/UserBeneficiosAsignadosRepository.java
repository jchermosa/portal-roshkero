package com.backend.portalroshkabackend.Repositories.UsuarioRepositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.portalroshkabackend.Models.BeneficiosAsignados;

@Repository
public interface UserBeneficiosAsignadosRepository extends JpaRepository<BeneficiosAsignados, Integer> {
    
}
