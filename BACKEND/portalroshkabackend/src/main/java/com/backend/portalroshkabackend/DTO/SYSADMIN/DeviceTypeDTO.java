package com.backend.portalroshkabackend.DTO.SYSADMIN;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceTypeDTO {

    private Integer idTipoDispositivo;
    private String nombre;
    private String detalle;
    
}
