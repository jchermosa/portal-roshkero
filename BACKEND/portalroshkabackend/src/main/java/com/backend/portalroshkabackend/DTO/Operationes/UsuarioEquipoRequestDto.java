package com.backend.portalroshkabackend.DTO.Operationes;

import java.sql.Date;
import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Data
public class UsuarioEquipoRequestDto {

    private Integer id_asignacion_usuario_equipo;

    @NotNull
    private LocalDate fechaEntrada; // fecha_entrada

    @NotNull
    private LocalDate fechaFin; // fecha_fin

    @NotNull
    private Integer porcentajeTrabajo; // porcentaje_trabajo

    @NotNull
    private Integer idUsuario; // id_usuario

    @NotNull
    private Integer idTecnologia; // id_tecnologia

    @NotNull
    private Integer idEquipo; // id_equipo
}