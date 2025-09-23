package com.backend.portalroshkabackend.Services.Operations.Interface;

import org.springframework.data.domain.Pageable;

import com.backend.portalroshkabackend.DTO.Operationes.UsuarioEquipoRequestDto;
import com.backend.portalroshkabackend.DTO.Operationes.UsuariosEquipoCombinedResponseDto;
import com.backend.portalroshkabackend.DTO.Operationes.UsuariosEquipoResponseDto;

public interface IUsuarioisEquipoService {

    // Page<UsuariosEquipoResponseDto> getAllUsuariosEquipo(Integer idEquipo, Pageable pageable);
    UsuariosEquipoCombinedResponseDto getUsuariosEnYFueraDeEquipo(Integer idEquipo, Pageable pageable);
    UsuariosEquipoResponseDto addUsuarioToEquipo(Integer idEquipo, UsuarioEquipoRequestDto dto);
    // UsuariosEquipoResponseDto updateUsuarioEnEquipo(Integer idEquipo, Integer idUsuarioAsignacion, UsuarioEquipoRequestDto dto);
    // void removeUsuarioDeEquipo(Integer idEquipo, Integer idUsuarioAsignacion);


}
