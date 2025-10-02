package com.backend.portalroshkabackend.tools.validator.cargo;

import com.backend.portalroshkabackend.DTO.th.cargos.CargoUpdateDto;
import com.backend.portalroshkabackend.Repositories.TH.CargosRepository;
import com.backend.portalroshkabackend.tools.errors.errorslist.cargos.CargoDuplicateNameException;
import com.backend.portalroshkabackend.tools.validator.ValidatorStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CargoUniqueNameOnUpdateValidator implements ValidatorStrategy<CargoUpdateDto> {
    private final CargosRepository cargosRepository;

    @Autowired
    public CargoUniqueNameOnUpdateValidator(CargosRepository cargosRepository){
        this.cargosRepository = cargosRepository;
    }

    @Override
    public void validate(CargoUpdateDto dto) {
        boolean exists = cargosRepository.existsByNombreAndIdCargoNot(dto.getNombre(), dto.getIdCargo());

        if (exists) throw new CargoDuplicateNameException(dto.getNombre());
    }
}
