package com.backend.portalroshkabackend.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.portalroshkabackend.Models.Roles;
import com.backend.portalroshkabackend.Repositories.RolesRepository;

@Service
public class RolesService {


    @Autowired
    private RolesRepository rolesRepository; 

    RolesService(RolesRepository rolesRepository) {
        this.rolesRepository = rolesRepository;
    }

    public Iterable<Roles> getAllRoles() {
        return rolesRepository.findAll();
    }
    
}
