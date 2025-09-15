package com.backend.portalroshkabackend.DTO;

import java.sql.Date;

import com.backend.portalroshkabackend.Models.EstadoAsignacion;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InventarioAsignadoDto {
    private Date fechaAsignacion;
    private Date fechaDevolucion;
    private Integer idInventario;
    private EstadoAsignacion estado;
    private Integer idSolicitudDispositivos;
}
