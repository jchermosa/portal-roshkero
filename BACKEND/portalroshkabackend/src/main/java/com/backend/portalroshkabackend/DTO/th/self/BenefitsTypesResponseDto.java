package com.backend.portalroshkabackend.DTO.th.self;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class BenefitsTypesResponseDto {

    private Integer idBeneficio;

    private String nombre;

    private String descripcion;

    private LocalDate inicioVigencia;

    private LocalDate finVigencia;

    private LocalDateTime fechaCreacion;
}
