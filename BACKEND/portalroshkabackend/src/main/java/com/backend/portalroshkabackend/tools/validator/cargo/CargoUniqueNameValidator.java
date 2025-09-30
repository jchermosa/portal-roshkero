package com.backend.portalroshkabackend.tools.validator.cargo;

import com.backend.portalroshkabackend.DTO.th.cargos.CargoInsertDto;
import com.backend.portalroshkabackend.Repositories.TH.CargosRepository;
import com.backend.portalroshkabackend.tools.errors.errorslist.cargos.CargoDuplicateNameException;
import com.backend.portalroshkabackend.tools.validator.ValidatorStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CargoUniqueNameValidator implements ValidatorStrategy<CargoInsertDto> {
    private final CargosRepository cargosRepository;

    @Autowired
    public CargoUniqueNameValidator(CargosRepository cargosRepository){
        this.cargosRepository = cargosRepository;
    }

    @Override
    public void validate(CargoInsertDto dto) {
        boolean exists = cargosRepository.existsByNombre(dto.getNombre());

        if (exists) throw new CargoDuplicateNameException(dto.getNombre());
    }
}
