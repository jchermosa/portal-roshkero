package com.backend.portalroshkabackend.Repositories;

import com.backend.portalroshkabackend.Models.Clientes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientesRepository extends JpaRepository<Clientes, Integer> {

}