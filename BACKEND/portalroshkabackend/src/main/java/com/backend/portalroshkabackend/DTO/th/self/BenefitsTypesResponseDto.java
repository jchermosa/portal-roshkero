package com.backend.portalroshkabackend.DTO.th.self;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
public class BenefitsTypesResponseDto {

    private Integer idBeneficio;

    private String nombre;

    private String descripcion;

    private Date inicioVigencia;

    private Date finVigencia;

    private Date fechaCreacion;
}
