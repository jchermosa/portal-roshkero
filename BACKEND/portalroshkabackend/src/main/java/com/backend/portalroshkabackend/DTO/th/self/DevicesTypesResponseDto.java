package com.backend.portalroshkabackend.DTO.th.self;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DevicesTypesResponseDto {

    private Integer idInventario;

    private String nombre;

    private String detalle;

    private java.util.Date fechaCreacion;
}
