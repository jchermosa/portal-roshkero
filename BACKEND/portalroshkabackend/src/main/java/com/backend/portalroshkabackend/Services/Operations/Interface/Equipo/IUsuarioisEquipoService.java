package com.backend.portalroshkabackend.Services.Operations.Interface.Equipo;

import org.springframework.data.domain.Pageable;

import com.backend.portalroshkabackend.DTO.Operationes.UsuarioEquipoRequestDto;
import com.backend.portalroshkabackend.DTO.Operationes.UsuariosEquipoCombinedResponseDto;
import com.backend.portalroshkabackend.DTO.Operationes.Metadatas.UsuariosEquipoResponseDto;

public interface IUsuarioisEquipoService {

    UsuariosEquipoCombinedResponseDto getUsuariosEnYFueraDeEquipo(Integer idEquipo, Pageable pageable);
    UsuariosEquipoResponseDto addUsuarioToEquipo(Integer idEquipo, UsuarioEquipoRequestDto dto);

}
