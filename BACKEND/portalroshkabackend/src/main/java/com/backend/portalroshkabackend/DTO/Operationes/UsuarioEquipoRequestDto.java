package com.backend.portalroshkabackend.DTO.Operationes;

import java.time.LocalDate;

import lombok.Data;

@Data
public class UsuarioEquipoRequestDto {

    private Integer id_asignacion_usuario_equipo;

    private LocalDate fechaEntrada; // fecha_entrada

    private LocalDate fechaFin; // fecha_fin

    private Integer porcentajeTrabajo; // porcentaje_trabajo

    private Integer idUsuario; // id_usuario

    private Integer idTecnologia; // id_tecnologia

    private Integer idEquipo; // id_equipo
}