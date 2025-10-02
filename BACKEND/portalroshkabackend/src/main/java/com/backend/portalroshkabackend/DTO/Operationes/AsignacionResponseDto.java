package com.backend.portalroshkabackend.DTO.Operationes;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class AsignacionResponseDto {
    private Integer idAsignacionUsuarioEquipo;
    private LocalDate fechaEntrada;
    private LocalDate fechaFin;
    private Integer porcentajeTrabajo;
    private UsuarioisResponseDto usuario;
    private EquiposResponseDto equipo;
    private LocalDateTime fechaCreacion;
}