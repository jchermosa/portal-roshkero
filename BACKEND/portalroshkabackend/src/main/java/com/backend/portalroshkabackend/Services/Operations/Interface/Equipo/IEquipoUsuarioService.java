package com.backend.portalroshkabackend.Services.Operations.Interface.Equipo;

import java.util.List;

import com.backend.portalroshkabackend.DTO.Operationes.UsuarioAsignacionDto;
import com.backend.portalroshkabackend.DTO.Operationes.UsuarioisResponseDto;

public interface IEquipoUsuarioService {
    List<UsuarioAsignacionDto> getUsuariosAsignacion(Integer equipoId);
    List<UsuarioisResponseDto> getUsuariosNoEnEquipo(Integer equipoId);
}
