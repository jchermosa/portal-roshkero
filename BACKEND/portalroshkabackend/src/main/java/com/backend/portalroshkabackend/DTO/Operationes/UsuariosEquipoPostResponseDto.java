package com.backend.portalroshkabackend.DTO.Operationes;

import java.time.LocalDate;

import com.backend.portalroshkabackend.Models.Equipos;
import com.backend.portalroshkabackend.Models.Tecnologias;
import lombok.Data;

@Data
public class UsuariosEquipoPostResponseDto {
    private Integer idAsignacionUsuarioEquipo;
    private Integer idUsuario; // просто id пользователя
    private Tecnologias idTecnologia;
    private Equipos equipos;

    private LocalDate fechaEntrada;
    private LocalDate fechaFin;
    private Double porcentajeTrabajo;
    private LocalDate fechaCreacion;
}
