package com.backend.portalroshkabackend.DTO.SYSADMIN;

import java.time.LocalDate;

import com.backend.portalroshkabackend.Models.Enum.EstadoAsignacion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceAssignmentDTO {


    private Integer idDispositivoAsignado;
    private Integer idDispositivo;
    private Integer idSolicitud;
    private LocalDate fechaEntrega;
    private LocalDate fechaDevolucion;
    private EstadoAsignacion estadoAsignacion;
    private String observaciones;

    
}
