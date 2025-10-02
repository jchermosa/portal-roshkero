package com.backend.portalroshkabackend.tools.validator.employee.insert;

import com.backend.portalroshkabackend.DTO.common.UserInsertDto;
import com.backend.portalroshkabackend.Repositories.TH.CargosRepository;
import com.backend.portalroshkabackend.Repositories.TH.RolesRepository;
import com.backend.portalroshkabackend.tools.errors.errorslist.cargos.CargoNotFoundException;
import com.backend.portalroshkabackend.tools.errors.errorslist.roles.RolesNotFoundException;
import com.backend.portalroshkabackend.tools.validator.ValidatorStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RelatedEntitiesValidator implements ValidatorStrategy<UserInsertDto> {
    private final RolesRepository rolesRepository;
    private final CargosRepository cargosRepository;

    @Autowired
    public RelatedEntitiesValidator(RolesRepository rolesRepository,
                                    CargosRepository cargosRepository){
        this.rolesRepository = rolesRepository;
        this.cargosRepository = cargosRepository;
    }

    @Override
    public void validate(UserInsertDto dto) {
        if (!rolesRepository.existsById(dto.getRol().getIdRol())) throw new RolesNotFoundException(dto.getRol().getIdRol());

        if (!cargosRepository.existsById(dto.getCargo().getIdCargo())) throw new CargoNotFoundException(dto.getCargo().getIdCargo());
    }
}
