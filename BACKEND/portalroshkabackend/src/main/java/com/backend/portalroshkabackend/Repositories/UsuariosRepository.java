package com.backend.portalroshkabackend.Repositories;


import com.backend.portalroshkabackend.Models.Usuarios;

import org.springframework.stereotype.Repository;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

@Repository
public interface UsuariosRepository extends CrudRepository<Usuarios, Integer> {
    Optional<Usuarios> findByNroCedula(Integer nroCedula);
    Optional<Usuarios> findByCorreo(String correo);
}

