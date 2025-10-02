package com.backend.portalroshkabackend.Services.Operations.Interface;

import java.util.List;
import java.util.Map;

import com.backend.portalroshkabackend.DTO.Operationes.UsuarioAsignacionDto;
import com.backend.portalroshkabackend.Models.Equipos;
import com.backend.portalroshkabackend.Models.Usuario;

public interface IUsuarioService {

    void ajustarDisponibilidadConDelta(Usuario usuario, float delta);

    List<UsuarioAsignacionDto> updateUsers(Equipos equipo, List<UsuarioAsignacionDto> usuariosDto);

    List<UsuarioAsignacionDto> assignUsers(Integer equipoId, List<UsuarioAsignacionDto> usuariosDto);

    List<UsuarioAsignacionDto> calculateprocenteusuarios(List<UsuarioAsignacionDto> usuariosDto);
}
