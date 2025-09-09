package com.backend.portalroshkabackend.Repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.backend.portalroshkabackend.Models.Equipos;

@Repository
public interface EquiposRepository extends CrudRepository<Equipos, Integer> {


    

}
