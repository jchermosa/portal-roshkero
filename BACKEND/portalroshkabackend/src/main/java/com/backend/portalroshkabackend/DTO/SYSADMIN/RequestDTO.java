package com.backend.portalroshkabackend.DTO.SYSADMIN;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestDTO {
    
    private Integer idSolicitud;
    private Integer idUsuario;
    private Integer idDocumentoAdjunto;
    private Integer idLider;
    private String tipoSolicitud;
    private String comentario;
    private String estado;
    private LocalDate fechaInicio;
    private int cantDias;
    private LocalDate fechaFin;

}
