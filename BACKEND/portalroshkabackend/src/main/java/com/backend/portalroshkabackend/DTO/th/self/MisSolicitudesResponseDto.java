package com.backend.portalroshkabackend.DTO.th.self;

import com.backend.portalroshkabackend.Models.Enum.EstadoSolicitudEnum;
import com.backend.portalroshkabackend.Models.Enum.SolicitudesEnum;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class MisSolicitudesResponseDto {

    private Integer idSolicitudTH;

    private SolicitudesEnum solicitudThTipo;

    private String comentario;

    private Boolean aprobacionTH;

    private EstadoSolicitudEnum estado;

    private LocalDateTime fechaCreacion;

}
