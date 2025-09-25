package com.backend.portalroshkabackend.tools.validator;

import com.backend.portalroshkabackend.Models.Cargos;
import com.backend.portalroshkabackend.Models.Enum.EstadoActivoInactivo;
import com.backend.portalroshkabackend.Models.Enum.EstadoSolicitudEnum;
import com.backend.portalroshkabackend.Models.Roles;
import com.backend.portalroshkabackend.Repositories.OP.EquiposRepository;
import com.backend.portalroshkabackend.Repositories.TH.CargosRepository;
import com.backend.portalroshkabackend.Repositories.TH.RolesRepository;
import com.backend.portalroshkabackend.Repositories.TH.SolicitudRepository;
import com.backend.portalroshkabackend.Repositories.TH.UserRepository;
import com.backend.portalroshkabackend.tools.errors.errorslist.cargos.CargoNotFoundException;
import com.backend.portalroshkabackend.tools.errors.errorslist.roles.RolesNotFoundException;
import com.backend.portalroshkabackend.tools.errors.errorslist.user.*;
import org.springframework.stereotype.Component;

@Component //Esta clase se debe inyectar por constructor en las clases que la quieren utilizar
public class EmployeeValidator {
    private final UserRepository userRepository;
    private final RolesRepository rolesRepository;
    private final CargosRepository cargosRepository;
    private final SolicitudRepository solicitudRepository;

    public EmployeeValidator(UserRepository userRepository,
                             RolesRepository rolesRepository,
                             EquiposRepository equiposRepository,
                             CargosRepository cargosRepository,
                             SolicitudRepository solicitudRepository){
        this.userRepository = userRepository;
        this.rolesRepository = rolesRepository;
        this.cargosRepository = cargosRepository;
        this.solicitudRepository = solicitudRepository;
    }

    public void validateUniqueEmail(String correo, Integer excludeUserId){
        boolean exists = (excludeUserId == null)
                ? userRepository.existsByCorreo(correo)
                : userRepository.existsByCorreoAndIdUsuarioNot(correo, excludeUserId);

        if (exists){
            throw new DuplicateEmailException(correo);
        }
    }

    public void validateUniqueCedula(String nroCedula, Integer excludeUserId){
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
        boolean havePendientRequests = solicitudRepository.existsByUsuario_idUsuarioAndEstado(idUsuario, EstadoSolicitudEnum.P);

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
