package com.backend.portalroshkabackend.Services.Operations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.backend.portalroshkabackend.DTO.Operationes.RolesResponseDto;
import com.backend.portalroshkabackend.Repositories.RolesRepository;

@Service("operationsRolesService")
public class RolesServiceImpl implements IRolesService {
    private final RolesRepository rolesRepository;

    @Autowired
    public RolesServiceImpl(RolesRepository rolesRepository) {
        this.rolesRepository = rolesRepository;
    }

    @Override
    public Page<RolesResponseDto> getAllRols(Pageable pageable) {
        return rolesRepository.findAll(pageable)
                .map(roles -> {
                    RolesResponseDto Dto = new RolesResponseDto();
                    Dto.setIdRole(roles.getIdRol());
                    Dto.setNombre(roles.getNombre());
                    return Dto;
                });
    }
}
