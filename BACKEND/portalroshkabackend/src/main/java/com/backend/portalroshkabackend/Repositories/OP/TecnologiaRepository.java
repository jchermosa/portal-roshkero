package com.backend.portalroshkabackend.Repositories.OP;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.portalroshkabackend.Models.Tecnologias;

public interface TecnologiaRepository extends JpaRepository<Tecnologias, Integer> {
    Tecnologias findByIdTecnologia(Integer idTecnologia);
}
