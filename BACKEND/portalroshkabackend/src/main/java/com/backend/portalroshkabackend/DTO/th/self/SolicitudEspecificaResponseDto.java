package com.backend.portalroshkabackend.DTO.th.self;

import com.backend.portalroshkabackend.Models.Enum.EstadoSolicitudEnum;
import com.backend.portalroshkabackend.Models.SolicitudThTipo;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class SolicitudEspecificaResponseDto {
    private Integer idSolicitudTH;

    private SolicitudThTipo solicitudThTipo;

    private Integer cantidadDias;

    private LocalDate fechaInicio;

    private String comentario;

}
