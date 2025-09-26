package com.backend.portalroshkabackend.tools.validator;

import com.backend.portalroshkabackend.Repositories.TH.RolesRepository;
import com.backend.portalroshkabackend.Repositories.TH.UserRepository;
import com.backend.portalroshkabackend.tools.errors.errorslist.roles.RoleAssignedToUsersException;
import com.backend.portalroshkabackend.tools.errors.errorslist.roles.RoleDuplicateNameException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RolValidator {
    private final RolesRepository rolesRepository;
    private final UserRepository userRepository;

    @Autowired
    public RolValidator(RolesRepository rolesRepository,
                          UserRepository userRepository){
        this.rolesRepository = rolesRepository;
        this.userRepository = userRepository;
    }

    public void validateRolDontHaveUsersAssigned (Integer idRol, String rolName){
        boolean haveUsersAsociated = userRepository.existsByRol_IdRol(idRol);

        if (haveUsersAsociated) throw new RoleAssignedToUsersException(rolName);

    }

    public void validateRolUniqueName(String nombre, Integer excludeRolId){
        boolean exists = (excludeRolId == null)
                ? rolesRepository.existsByNombre(nombre)
                : rolesRepository.existsByNombreAndIdRolNot(nombre, excludeRolId);
        if (exists) throw new RoleDuplicateNameException(nombre);
    }
}
