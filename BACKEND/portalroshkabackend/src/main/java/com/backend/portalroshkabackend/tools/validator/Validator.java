package com.backend.portalroshkabackend.tools.validator;

import com.backend.portalroshkabackend.Models.Cargos;
import com.backend.portalroshkabackend.Models.Roles;
import com.backend.portalroshkabackend.Repositories.CargosRepository;
import com.backend.portalroshkabackend.Repositories.EquiposRepository;
import com.backend.portalroshkabackend.Repositories.RolesRepository;
import com.backend.portalroshkabackend.Repositories.UserRepository;
import com.backend.portalroshkabackend.tools.errors.errorslist.*;
import org.springframework.stereotype.Component;

@Component //Esta clase se debe inyectar por constructor en las clases que la quieren utilizar
public class Validator {
    private final UserRepository userRepository;
    private final RolesRepository rolesRepository;
    private final CargosRepository cargosRepository;

    public Validator(UserRepository userRepository,
                     RolesRepository rolesRepository,
                     EquiposRepository equiposRepository,
                     CargosRepository cargosRepository){
        this.userRepository = userRepository;
        this.rolesRepository = rolesRepository;
        this.cargosRepository = cargosRepository;
    }

    public void validateUniqueEmail(String correo, Integer excludeUserId){
        boolean exists = (excludeUserId == null)
                ? userRepository.existsByCorreo(correo)
                : userRepository.existsByCorreoAndIdUsuarioNot(correo, excludeUserId);

        if (exists){
            throw new DuplicateEmailException(correo);
        }
    }

    public void validateUniqueCedula(Integer nroCedula, Integer excludeUserId){
        boolean exists = (excludeUserId == null)
                ? userRepository.existsByNroCedula(nroCedula)
                : userRepository.existsByNroCedulaAndIdUsuarioNot(nroCedula, excludeUserId);

        if (exists){
            throw new DuplicateCedulaException(nroCedula);
        }
    }

    public void validateUniquePhone(String phone, Integer excludeUserId){
        boolean exists = (excludeUserId == null)
                ? userRepository.existsByTelefono(phone)
                : userRepository.existsByTelefonoAndIdUsuarioNot(phone, excludeUserId);

        if (exists){
            throw new DuplicateTelefonoException(phone);
        }
    }

    public void validateRelatedEntities(Roles idRol, Cargos idCargo){
        if (!rolesRepository.existsById(idRol.getIdRol())) throw new RolesNotFoundException(idRol.getIdRol());

        //if (!equiposRepository.existsById(idEquipo)) throw new EquipoNotFoundException(idEquipo);

        if (!cargosRepository.existsById(idCargo.getIdCargo())) throw new CargoNotFoundException(idCargo.getIdCargo());
    }


}
