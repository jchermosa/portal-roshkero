package com.backend.portalroshkabackend.DTO.Operationes;

import java.util.List;

import org.springframework.data.domain.Page;

import com.backend.portalroshkabackend.DTO.Operationes.Metadatas.UsuariosEquipoResponseDto;
import com.backend.portalroshkabackend.Models.Tecnologias;

public class UsuariosEquipoCombinedResponseDto {
    private Page<UsuariosEquipoResponseDto> usuariosEnEquipo; // с пагинацией
    private List<UsuarioisResponseDto> usuariosFueraEquipo; // без пагинации
    private List<Tecnologias> tecnologias; // новый список всех технологий

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

    public List<Tecnologias> getTecnologias() {
        return tecnologias;
    }

    public void setTecnologias(List<Tecnologias> tecnologias) {
        this.tecnologias = tecnologias;
    }
}
