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
    private String nombre;
    private String apellido;
    private String correo;
    private Float porcentajeTrabajo;
    private Date fechaEntrada;
    private Date fechaFin;
}
