package com.backend.portalroshkabackend.Services.HumanResource;

import com.backend.portalroshkabackend.DTO.th.cargos.CargoByIdResponseDto;
import com.backend.portalroshkabackend.DTO.th.cargos.CargoInsertDto;
import com.backend.portalroshkabackend.DTO.th.cargos.CargosDefaultResponseDto;
import com.backend.portalroshkabackend.DTO.th.cargos.CargosResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ICommonRolesCargosService<T, TById, TDefault, TI, TU> { // T: ResponseDto, TById: ByIdResponseDto, TDefault: DefaultResponseDto, TI: InsertDto, TU: UpdateDto
    Page<T> getAll(Pageable pageable);
    TById getById(int id);
    TDefault add(TI insertDto);
    TDefault update(int id, TU updateDto);
    TDefault delete(int id);
}
