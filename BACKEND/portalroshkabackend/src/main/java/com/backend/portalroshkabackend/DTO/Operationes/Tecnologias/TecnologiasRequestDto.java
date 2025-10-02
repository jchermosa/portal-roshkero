package com.backend.portalroshkabackend.DTO.Operationes.Tecnologias;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TecnologiasRequestDto {
    private String nombre;
    private String descripcion;
}