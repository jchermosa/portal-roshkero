package com.backend.portalroshkabackend.Repositories;

import com.backend.portalroshkabackend.Models.TipoBeneficios;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BeneficiosRepository extends JpaRepository<TipoBeneficios, Integer> {

}
