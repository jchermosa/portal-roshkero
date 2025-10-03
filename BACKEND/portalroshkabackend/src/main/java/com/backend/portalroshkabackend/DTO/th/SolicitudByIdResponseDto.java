package com.backend.portalroshkabackend.DTO.th;

import com.backend.portalroshkabackend.Models.Enum.EstadoSolicitudEnum;
import com.backend.portalroshkabackend.Models.Enum.SolicitudesEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class SolicitudByIdResponseDto {

    private int idSolicitud;

    private String usuario;

    private String documentoAdjunto;

    private String lider;

    private SolicitudesEnum tipoSolicitud;

    private String comentario;

    private EstadoSolicitudEnum estado;

    private LocalDate fechaInicio;

    private BigDecimal monto;

    private int cantDias;

    private LocalDate fechaFin;

    private LocalDateTime fechaCreacion;
}
