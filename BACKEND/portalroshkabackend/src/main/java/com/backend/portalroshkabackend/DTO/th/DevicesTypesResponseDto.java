package com.backend.portalroshkabackend.DTO.th;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
