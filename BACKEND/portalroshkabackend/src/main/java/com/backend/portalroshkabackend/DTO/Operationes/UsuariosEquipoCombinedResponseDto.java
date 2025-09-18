package com.backend.portalroshkabackend.DTO.Operationes;

import java.util.List;

import org.springframework.data.domain.Page;

import com.backend.portalroshkabackend.DTO.Operationes.UsuariosEquipoResponseDto;
import com.backend.portalroshkabackend.DTO.Operationes.UsuarioisResponseDto;

import java.util.List;
import org.springframework.data.domain.Page;

public class UsuariosEquipoCombinedResponseDto {
    private Page<UsuariosEquipoResponseDto> usuariosEnEquipo; // с пагинацией
    private List<UsuarioisResponseDto> usuariosFueraEquipo; // без пагинации

    public Page<UsuariosEquipoResponseDto> getUsuariosEnEquipo() {
        return usuariosEnEquipo;
    }

    public void setUsuariosEnEquipo(Page<UsuariosEquipoResponseDto> usuariosEnEquipo) {
        this.usuariosEnEquipo = usuariosEnEquipo;
    }

    public List<UsuarioisResponseDto> getUsuariosFueraEquipo() {
        return usuariosFueraEquipo;
    }

    public void setUsuariosFueraEquipo(List<UsuarioisResponseDto> usuariosFueraEquipo) {
        this.usuariosFueraEquipo = usuariosFueraEquipo;
    }
}
