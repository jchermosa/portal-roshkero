package com.backend.portalroshkabackend.Services.Operations;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.backend.portalroshkabackend.DTO.Operationes.UsuariosEquipoResponseDto;

public interface IUsuarioisEquipoService {

    Page<UsuariosEquipoResponseDto> getAllUsuariosEquipo(Integer idEquipo, Pageable pageable);

}
