package com.backend.portalroshkabackend.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

import com.backend.portalroshkabackend.Models.Roles;
import com.backend.portalroshkabackend.Repositories.RolesRepository;
import com.backend.portalroshkabackend.DTO.Operationes.RolesResponseDto;;

/* 
@Service
public class RolesService {

    @Autowired
    private RolesRepository rolesRepository; 

    RolesService(RolesRepository rolesRepository) {
        this.rolesRepository = rolesRepository;
    }

    public List<RolesResponseDto> getAllRoles() {
        List<Roles> roles = (List<Roles>) rolesRepository.findAll();
        return roles.stream().map(this::mapToRolesResponseDto).collect(Collectors.toList());
    }

    private RolesResponseDto mapToRolesResponseDto(Roles rol) {
        RolesResponseDto dto = new RolesResponseDto();
        dto.setIdRol(rol.getIdRol());
        dto.setNombre(rol.getNombre());
        return dto;
    }
    
}
    */
