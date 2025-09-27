package com.backend.portalroshkabackend.tools.validator.employee.update;

import com.backend.portalroshkabackend.DTO.common.UserUpdateDto;
import com.backend.portalroshkabackend.Repositories.TH.CargosRepository;
import com.backend.portalroshkabackend.Repositories.TH.RolesRepository;
import com.backend.portalroshkabackend.tools.errors.errorslist.cargos.CargoNotFoundException;
import com.backend.portalroshkabackend.tools.errors.errorslist.roles.RolesNotFoundException;
import com.backend.portalroshkabackend.tools.validator.ValidatorStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RelatedEntitiesOnUpdateValidator implements ValidatorStrategy<UserUpdateDto>  {
    private final RolesRepository rolesRepository;
    private final CargosRepository cargosRepository;

    @Autowired
    public RelatedEntitiesOnUpdateValidator(RolesRepository rolesRepository,
                                            CargosRepository cargosRepository){
        this.rolesRepository = rolesRepository;
        this.cargosRepository = cargosRepository;
    }

    @Override
    public void validate(UserUpdateDto dto) {
        if (!rolesRepository.existsById(dto.getRoles().getIdRol())) throw new RolesNotFoundException(dto.getRoles().getIdRol());

        if (!cargosRepository.existsById(dto.getCargos().getIdCargo())) throw new CargoNotFoundException(dto.getCargos().getIdCargo());
    }
}
