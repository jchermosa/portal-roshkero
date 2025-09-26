package com.backend.portalroshkabackend.DTO.Operationes;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipoAsignacionUpdateDiasUbicacionesDto {
    private Integer idEquipo;
    private List<DiaUbicacionDto> asignaciones;

    @Data
    @NoArgsConstructor
    public static class DiaUbicacionDto {
        private Integer idDiaLaboral;
        private Integer idUbicacion; // or null
    }
}