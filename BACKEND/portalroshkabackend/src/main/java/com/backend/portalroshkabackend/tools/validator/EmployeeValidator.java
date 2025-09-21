package com.backend.portalroshkabackend.tools.validator;

import com.backend.portalroshkabackend.Models.Cargos;
import com.backend.portalroshkabackend.Models.Enum.EstadoActivoInactivo;
import com.backend.portalroshkabackend.Models.Enum.EstadoSolicitudEnum;
import com.backend.portalroshkabackend.Models.Roles;
import com.backend.portalroshkabackend.Repositories.*;
import com.backend.portalroshkabackend.tools.errors.errorslist.cargos.CargoNotFoundException;
import com.backend.portalroshkabackend.tools.errors.errorslist.roles.RolesNotFoundException;
import com.backend.portalroshkabackend.tools.errors.errorslist.user.*;
import org.springframework.stereotype.Component;

@Component //Esta clase se debe inyectar por constructor en las clases que la quieren utilizar
public class EmployeeValidator {
    private final UserRepository userRepository;
    private final RolesRepository rolesRepository;
    private final CargosRepository cargosRepository;
    private final SolicitudesTHRepository solicitudesTHRepository;

    public EmployeeValidator(UserRepository userRepository,
                             RolesRepository rolesRepository,
                             EquiposRepository equiposRepository,
                             CargosRepository cargosRepository,
                             SolicitudesTHRepository solicitudesTHRepository){
        this.userRepository = userRepository;
        this.rolesRepository = rolesRepository;
        this.cargosRepository = cargosRepository;
        this.solicitudesTHRepository = solicitudesTHRepository;
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

    public void validateEmployeeDontHavePendientRequests(Integer idUsuario, String nombre, String apellido){
        boolean havePendientRequests = solicitudesTHRepository.existsByUsuario_idUsuarioAndEstado(idUsuario, EstadoSolicitudEnum.P);

        if (havePendientRequests){
            throw new UserHavePendientRequestsException(nombre, apellido);
        }
    }

    public void validateEmployeeIsActive(EstadoActivoInactivo estado, String userName, String userLastName){
        if (estado == EstadoActivoInactivo.I) throw new UserAlreadyInactiveException(userName, userLastName);
    }

    public void validateRelatedEntities(Roles idRol, Cargos idCargo){
        if (!rolesRepository.existsById(idRol.getIdRol())) throw new RolesNotFoundException(idRol.getIdRol());

        if (!cargosRepository.existsById(idCargo.getIdCargo())) throw new CargoNotFoundException(idCargo.getIdCargo());
    }


}
