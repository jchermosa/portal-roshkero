package com.backend.portalroshkabackend.DTO;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.backend.portalroshkabackend.Models.Enum.EstadoAsignacion;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InventarioAsignadoDto {
    private Integer idDispositivoAsignado;
    private LocalDate fechaAsignacion;
    private LocalDate fechaDevolucion;
    private LocalDateTime fechaCreacion;
    private Integer idInventario;
    private String nombreDispositivo;
    private EstadoAsignacion estado;
    private Integer idSolicitudDispositivos;
}
