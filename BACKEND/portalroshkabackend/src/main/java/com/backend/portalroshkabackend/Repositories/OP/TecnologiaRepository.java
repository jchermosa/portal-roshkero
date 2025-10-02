package com.backend.portalroshkabackend.Repositories.OP;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backend.portalroshkabackend.Models.Tecnologias;

@Repository
public interface TecnologiaRepository extends JpaRepository<Tecnologias, Integer> {
    Tecnologias findByIdTecnologia(Integer idTecnologia);
}
