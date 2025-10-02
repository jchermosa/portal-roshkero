package com.backend.portalroshkabackend.DTO.UsuarioDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class tiposDispositivosDto {

    private Integer idTipoDispositivo;
    private String nombre;
    private String detalle;
    
}
