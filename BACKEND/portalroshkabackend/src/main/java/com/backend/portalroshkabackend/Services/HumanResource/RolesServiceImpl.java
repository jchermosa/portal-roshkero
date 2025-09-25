package com.backend.portalroshkabackend.Services.HumanResource;

import com.backend.portalroshkabackend.DTO.th.cargos.CargoByIdResponseDto;
import com.backend.portalroshkabackend.DTO.th.cargos.CargoInsertDto;
import com.backend.portalroshkabackend.DTO.th.cargos.CargosDefaultResponseDto;
import com.backend.portalroshkabackend.DTO.th.cargos.CargosResponseDto;
import com.backend.portalroshkabackend.DTO.th.roles.RolByIdResponseDto;
import com.backend.portalroshkabackend.DTO.th.roles.RolDefaultResponseDto;
import com.backend.portalroshkabackend.DTO.th.roles.RolesResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service("rolesService") // Para hacer keyed dependency injection anashe
public class RolesServiceImpl implements ICommonRolesCargosService<RolesResponseDto, RolByIdResponseDto, RolDefaultResponseDto>{



    @Override
    public Page<RolesResponseDto> getAll(Pageable pageable) {
        return null;
    }

    @Override
    public RolByIdResponseDto getById(int idCargo) {
        return null;
    }

    @Override
    public RolDefaultResponseDto add(CargoInsertDto insertDto) {
        return null;
    }

    @Override
    public RolDefaultResponseDto update(int idCargo, CargoInsertDto updateDto) {
        return null;
    }

    @Override
    public RolDefaultResponseDto delete(int idCargo) {
        return null;
    }
}
