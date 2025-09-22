package com.backend.portalroshkabackend.DTO.th.self;

import lombok.Data;
import lombok.NoArgsConstructor;

import com.backend.portalroshkabackend.Models.Enum.EstadoActivoInactivo;

@Data
@NoArgsConstructor
public class BenefitsTypesResponseDto {

    private Integer idBeneficio;

    private String nombre;

    private String descripcion;

    private EstadoActivoInactivo vigencia;

}
