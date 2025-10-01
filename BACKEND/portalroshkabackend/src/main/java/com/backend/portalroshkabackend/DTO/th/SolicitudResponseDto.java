package com.backend.portalroshkabackend.DTO.th;

import com.backend.portalroshkabackend.Models.Enum.EstadoSolicitudEnum;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class SolicitudResponseDto {

    private Integer idSolicitud;

    private String usuario;

    private String tipoSolicitud;

    private String subTipo;

    private LocalDate fechaInicio;

    private Integer cantidadDias;

    private LocalDateTime fechaCreacion;

    private EstadoSolicitudEnum estado;

}
