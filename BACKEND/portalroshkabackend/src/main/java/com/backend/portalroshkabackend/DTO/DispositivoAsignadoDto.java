package com.backend.portalroshkabackend.DTO;

import java.sql.Date;

import com.backend.portalroshkabackend.Models.Enum.EstadoAsignacion;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DispositivoAsignadoDto {
    private Date fechaAsignacion;
    private Date fechaDevolucion;
    private Integer idInventario;
    private EstadoAsignacion estado;
    private Integer idSolicitudDispositivos;
}
