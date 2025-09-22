package com.backend.portalroshkabackend.DTO.th;

import com.backend.portalroshkabackend.Models.Enum.EstadoSolicitudEnum;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class SolicitudTHResponseDto {

    private Integer idSolicitudTH;

    private String usuario;

    private String solicitudThTipo;

    private LocalDate fechaInicio;

    private Integer cantidadDias;

    private LocalDateTime fechaCreacion;

    private EstadoSolicitudEnum estado;

}
