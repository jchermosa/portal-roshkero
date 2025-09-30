package com.backend.portalroshkabackend.tools.validator.rol;

import com.backend.portalroshkabackend.DTO.th.roles.RolUpdateDto;
import com.backend.portalroshkabackend.Repositories.TH.RolesRepository;
import com.backend.portalroshkabackend.tools.errors.errorslist.roles.RoleDuplicateNameException;
import com.backend.portalroshkabackend.tools.validator.ValidatorStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RoleUniqueNameOnUpdateValidator implements ValidatorStrategy<RolUpdateDto> {
    private final RolesRepository rolesRepository;

    @Autowired
    public RoleUniqueNameOnUpdateValidator(RolesRepository rolesRepository){
        this.rolesRepository = rolesRepository;
    }

    @Override
    public void validate(RolUpdateDto dto) {
        boolean exists = rolesRepository.existsByNombreAndIdRolNot(dto.getNombre(), dto.getIdRole());

        if (exists) throw new RoleDuplicateNameException(dto.getNombre());
    }
}
