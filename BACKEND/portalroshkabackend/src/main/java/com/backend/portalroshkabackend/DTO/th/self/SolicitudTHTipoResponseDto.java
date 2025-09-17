package com.backend.portalroshkabackend.DTO.th.self;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SolicitudTHTipoResponseDto {

    private Integer idSolicitudTHTipo;

    private String nombre;

    private java.sql.Date fechaCreacion;
}
