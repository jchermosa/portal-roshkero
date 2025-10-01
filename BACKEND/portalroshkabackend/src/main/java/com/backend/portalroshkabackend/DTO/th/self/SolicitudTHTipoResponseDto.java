package com.backend.portalroshkabackend.DTO.th.self;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SolicitudTHTipoResponseDto {

    private Integer idSolicitudTHTipo;

    private String nombre;

    private LocalDateTime fechaCreacion;
}
