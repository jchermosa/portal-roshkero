package com.backend.portalroshkabackend.Services.HumanResource;

import com.backend.portalroshkabackend.DTO.th.cargos.CargoByIdResponseDto;
import com.backend.portalroshkabackend.DTO.th.cargos.CargoInsertDto;
import com.backend.portalroshkabackend.DTO.th.cargos.CargosDefaultResponseDto;
import com.backend.portalroshkabackend.DTO.th.cargos.CargosResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICargosService {
    Page<CargosResponseDto> getAllCargos(Pageable pageable);
    CargoByIdResponseDto getCargoById(int idCargo);
    CargosDefaultResponseDto addCargo(CargoInsertDto insertDto);
    CargosDefaultResponseDto updateCargo(int idCargo, CargoInsertDto updateDto);
    CargosDefaultResponseDto deleteCargo(int idCargo);
}
