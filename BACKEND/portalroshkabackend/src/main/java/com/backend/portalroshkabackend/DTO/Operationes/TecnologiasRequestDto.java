package com.backend.portalroshkabackend.DTO.Operationes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TecnologiasRequestDto {

    private Integer idTecnologia;
    private String nombre;
    private String descripcion;

    
}
