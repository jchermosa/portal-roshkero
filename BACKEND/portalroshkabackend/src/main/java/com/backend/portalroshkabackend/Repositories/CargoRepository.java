package com.backend.portalroshkabackend.Repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.backend.portalroshkabackend.Models.Cargos;

@Repository
public interface CargoRepository extends CrudRepository<Cargos, Integer> {
    
    
}
