package com.backend.portalroshkabackend.DTO.Operationes;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UbicacionConDiasDto {
    private Integer idUbicacion;
    private String ubicacion;
    private List<DiasLaboralDto> diasLibres;
}
