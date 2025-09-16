package com.backend.portalroshkabackend.DTO.th;

import com.backend.portalroshkabackend.Models.Beneficios;
import com.backend.portalroshkabackend.Models.Enum.EstadoSolicitudEnum;
import com.backend.portalroshkabackend.Models.Permisos;
import com.backend.portalroshkabackend.Models.SolicitudThTipo;
import com.backend.portalroshkabackend.Models.Usuario;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.sql.Date;

@Data
@NoArgsConstructor
public class SolicitudTHResponseDto {

    private Integer idSolicitudTH;

    private Date fechaInicio;

    private Date fechaFin;

    private Usuario usuario;

    private Integer cantidadDias;

    private Boolean aprobacionTH;

    private String comentario;

    private SolicitudThTipo solicitudThTipo;

    private EstadoSolicitudEnum estado;

    private Date fechaCreacion;

    private Permisos permisos;

    private Beneficios beneficios;

}
