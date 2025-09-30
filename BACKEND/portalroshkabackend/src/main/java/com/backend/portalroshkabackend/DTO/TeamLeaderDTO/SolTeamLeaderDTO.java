package com.backend.portalroshkabackend.DTO.TeamLeaderDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.backend.portalroshkabackend.Models.Enum.EstadoSolicitudEnum;
import com.backend.portalroshkabackend.Models.Enum.SolicitudesEnum;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SolTeamLeaderDTO {

    private Integer idSolicitud;

    private Integer idUsuario;

    private SolicitudesEnum tipoSolicitud;

    private String comentario;

    private EstadoSolicitudEnum estado;

    private LocalDate fechaInicio;

    private Integer cantDias;

    private LocalDate fechaFin;

    private LocalDateTime fechaCreacion;

}
