package com.backend.portalroshkabackend.DTO.Operationes;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioAsignacionDto {
    private Integer idUsuario;
    private Float porcentajeTrabajo;
    private Date fechaEntrada;
    private Date fechaFin;
    private String estado;
}
