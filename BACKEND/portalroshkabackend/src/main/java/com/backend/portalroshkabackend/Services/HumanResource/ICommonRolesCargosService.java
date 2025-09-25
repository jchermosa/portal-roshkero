package com.backend.portalroshkabackend.Services.HumanResource;

import com.backend.portalroshkabackend.DTO.th.cargos.CargoByIdResponseDto;
import com.backend.portalroshkabackend.DTO.th.cargos.CargoInsertDto;
import com.backend.portalroshkabackend.DTO.th.cargos.CargosDefaultResponseDto;
import com.backend.portalroshkabackend.DTO.th.cargos.CargosResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ICommonRolesCargosService<T, TById, TDefault> { // T: ResponseDto, TById: ByIdResponseDto, TDefault: DefaultResponseDto
    Page<T> getAll(Pageable pageable);
    TById getById(int idCargo);
    TDefault add(CargoInsertDto insertDto);
    TDefault update(int idCargo, CargoInsertDto updateDto);
    TDefault delete(int idCargo);
}
