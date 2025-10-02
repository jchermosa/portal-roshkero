package com.backend.portalroshkabackend.tools.validator.rol;

import com.backend.portalroshkabackend.Models.Roles;
import com.backend.portalroshkabackend.Repositories.TH.UserRepository;
import com.backend.portalroshkabackend.tools.errors.errorslist.roles.RoleAssignedToUsersException;
import com.backend.portalroshkabackend.tools.validator.ValidatorStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RoleHasUserAssignedValidator implements ValidatorStrategy<Roles> {
    private final UserRepository userRepository;

    @Autowired
    public RoleHasUserAssignedValidator(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public void validate(Roles rol) {
        boolean hasAssigned = userRepository.existsByRol_IdRol(rol.getIdRol());

        if (hasAssigned) throw new RoleAssignedToUsersException(rol.getNombre());

    }
}
