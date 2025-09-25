package com.backend.portalroshkabackend.DTO.Operationes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipoDiaUbicacionResponceDto {
    private DiaLaboralDto diaLaboral;
    private UbicacionDto ubicacion;
}
