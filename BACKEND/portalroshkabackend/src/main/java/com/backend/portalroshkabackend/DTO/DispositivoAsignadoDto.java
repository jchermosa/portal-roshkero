package com.backend.portalroshkabackend.DTO;

import java.time.LocalDate;

import com.backend.portalroshkabackend.Models.Enum.EstadoAsignacion;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DispositivoAsignadoDto {
    private LocalDate fechaAsignacion;
    private LocalDate fechaDevolucion;
    private Integer idInventario;
    private EstadoAsignacion estado;
    private Integer idSolicitudDispositivos;
}
