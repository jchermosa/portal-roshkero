package com.backend.portalroshkabackend.DTO.th;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SolicitudTHTipoResponseDto {

    private Integer idSolicitudTHTipo;

    private String nombre;

    private java.sql.Date fechaCreacion;
}
