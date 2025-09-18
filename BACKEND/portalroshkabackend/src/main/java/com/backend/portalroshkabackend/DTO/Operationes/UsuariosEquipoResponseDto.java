package com.backend.portalroshkabackend.DTO.Operationes;

import java.sql.Date;

import com.backend.portalroshkabackend.Models.Equipos;
import com.backend.portalroshkabackend.Models.Tecnologias;
// import com.backend.portalroshkabackend.Models.Equipos;
import com.backend.portalroshkabackend.Models.Usuario;
import lombok.Data;

@Data
public class UsuariosEquipoResponseDto {
    private Integer idAsignacionUsuarioEquipo;

    private Date fechaEntrada;

    private Date fechaFin;

    private Float porcentajeTrabajo;

    private Usuario idUsuario;

    private Tecnologias idTecnologia;

    private Equipos equipos;

    private Date fechaCreacion;
}
