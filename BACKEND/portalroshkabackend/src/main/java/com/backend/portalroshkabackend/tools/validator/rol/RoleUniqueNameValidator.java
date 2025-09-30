package com.backend.portalroshkabackend.tools.validator.rol;

import com.backend.portalroshkabackend.DTO.th.cargos.CargoInsertDto;
import com.backend.portalroshkabackend.DTO.th.roles.RolInsertDto;
import com.backend.portalroshkabackend.Repositories.TH.RolesRepository;
import com.backend.portalroshkabackend.tools.errors.errorslist.roles.RoleDuplicateNameException;
import com.backend.portalroshkabackend.tools.validator.ValidatorStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RoleUniqueNameValidator implements ValidatorStrategy<RolInsertDto> {
    private final RolesRepository rolesRepository;

    @Autowired
    public RoleUniqueNameValidator(RolesRepository rolesRepository){
        this.rolesRepository = rolesRepository;
    }

    @Override
    public void validate(RolInsertDto dto) {
        boolean exists = rolesRepository.existsByNombre(dto.getNombre());

        if (exists) throw new RoleDuplicateNameException(dto.getNombre());
    }
}
