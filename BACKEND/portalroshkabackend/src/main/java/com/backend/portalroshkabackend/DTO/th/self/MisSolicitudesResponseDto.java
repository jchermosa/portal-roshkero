package com.backend.portalroshkabackend.DTO.th.self;

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
public class MisSolicitudesResponseDto {

    private Integer idSolicitudTH;

    private SolicitudThTipo solicitudThTipo;

    private String comentario;

    private Boolean aprobacionTH;

    private EstadoSolicitudEnum estado;

    private Date fechaCreacion;

}
