package com.backend.portalroshkabackend.DTO.Operationes.Metadatas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TecnologiasResponseDto {
    private Integer idTecnologia;
    private String nombre;
    private String descripcion;
}
