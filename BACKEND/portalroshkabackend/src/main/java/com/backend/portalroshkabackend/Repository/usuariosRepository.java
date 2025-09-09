package com.backend.portalroshkabackend.Repository;


import com.backend.portalroshkabackend.Models.usuarios;

import org.springframework.stereotype.Repository;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

@Repository
public interface usuariosRepository extends CrudRepository<usuarios, Integer> {
    Optional<usuarios> findByNroCedula(Integer nroCedula);
}

