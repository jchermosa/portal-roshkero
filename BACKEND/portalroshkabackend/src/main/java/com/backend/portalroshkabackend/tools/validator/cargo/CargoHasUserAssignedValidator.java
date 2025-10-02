package com.backend.portalroshkabackend.tools.validator.cargo;

import com.backend.portalroshkabackend.Models.Cargos;
import com.backend.portalroshkabackend.Repositories.TH.UserRepository;
import com.backend.portalroshkabackend.tools.errors.errorslist.cargos.CargoAssignedToUsersException;
import com.backend.portalroshkabackend.tools.validator.ValidatorStrategy;
import org.springframework.stereotype.Component;

@Component
public class CargoHasUserAssignedValidator implements ValidatorStrategy<Cargos> {
    private final UserRepository userRepository;

    public CargoHasUserAssignedValidator(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public void validate(Cargos cargo) {
        boolean hasUsers = userRepository.existsByCargo_IdCargo(cargo.getIdCargo());

        if (hasUsers) throw new CargoAssignedToUsersException(cargo.getNombre());
    }
}
