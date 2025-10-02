package com.backend.portalroshkabackend.DTO.th.self;

import com.backend.portalroshkabackend.Models.SolicitudThTipo;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@NoArgsConstructor
public class UpdateSolicitudDto {

    private SolicitudThTipo solicitudThTipo;

    private Integer cantidadDias;

    private LocalDate fechaInicio;

    private String comentario;

}
