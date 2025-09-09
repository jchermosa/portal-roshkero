package com.backend.portalroshkabackend.Repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.backend.portalroshkabackend.Models.Roles;

@Repository
public interface RolesRepository extends CrudRepository<Roles, Integer>    {


    
}

