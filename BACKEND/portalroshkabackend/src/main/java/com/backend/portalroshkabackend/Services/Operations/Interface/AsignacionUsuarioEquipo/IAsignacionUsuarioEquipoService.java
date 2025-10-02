package com.backend.portalroshkabackend.Services.Operations.Interface.AsignacionUsuarioEquipo;

import java.util.List;

import com.backend.portalroshkabackend.DTO.Operationes.UsuarioAsignacionDto;
import com.backend.portalroshkabackend.DTO.Operationes.UsuarioisResponseDto;

public interface IAsignacionUsuarioEquipoService {
    List<UsuarioAsignacionDto> getUsuariosAsignacion(Integer idEquipo);

    List<UsuarioisResponseDto> getUsuariosFueraEquipo(Integer idEquipo);
}
