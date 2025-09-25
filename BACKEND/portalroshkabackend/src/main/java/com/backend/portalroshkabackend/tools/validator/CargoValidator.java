package com.backend.portalroshkabackend.tools.validator;

import com.backend.portalroshkabackend.Repositories.CargosRepository;
import com.backend.portalroshkabackend.Repositories.UserRepository;
import com.backend.portalroshkabackend.tools.errors.errorslist.cargos.CargoAssignedToUsers;
import com.backend.portalroshkabackend.tools.errors.errorslist.cargos.CargoDuplicateNameException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CargoValidator {
    private final CargosRepository cargosRepository;
    private final UserRepository userRepository;

    @Autowired
    public CargoValidator(CargosRepository cargosRepository,
                          UserRepository userRepository){
        this.cargosRepository = cargosRepository;
        this.userRepository = userRepository;
    }

    public void validateCargoDontHaveUsersAssigned (Integer idCargo, String cargoName){
        boolean haveUsersAsociated = userRepository.existsByCargo_IdCargo(idCargo);

        if (haveUsersAsociated) throw new CargoAssignedToUsers(cargoName);

    }

    public void validateCargoUniqueName(String nombre, Integer excludeCargoId){
        boolean exists = (excludeCargoId == null)
                ? cargosRepository.existsByNombre(nombre)
                : cargosRepository.existsByNombreAndIdCargoNot(nombre, excludeCargoId);
        if (exists) throw new CargoDuplicateNameException(nombre);
    }
}
