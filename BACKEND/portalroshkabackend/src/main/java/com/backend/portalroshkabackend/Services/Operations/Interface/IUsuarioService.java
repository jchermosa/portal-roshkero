package com.backend.portalroshkabackend.Services.Operations.Interface;

import java.util.List;
import java.util.Map;

import com.backend.portalroshkabackend.DTO.Operationes.UsuarioAsignacionDto;
import com.backend.portalroshkabackend.Models.Equipos;
import com.backend.portalroshkabackend.Models.Usuario;

public interface IUsuarioService {
    Usuario getUsuarioById(Integer id);

    void ajustarDisponibilidadConDelta(Usuario usuario, float delta);

    void devolverDisponibilidad(Usuario usuario, float delta);

    List<UsuarioAsignacionDto> updateUsers(Equipos equipo, List<UsuarioAsignacionDto> usuariosDto);

    List<UsuarioAsignacionDto> assignUsers(Equipos equipo, List<UsuarioAsignacionDto> usuariosDto);
}
