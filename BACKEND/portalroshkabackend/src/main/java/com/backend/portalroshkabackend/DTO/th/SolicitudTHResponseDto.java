package com.backend.portalroshkabackend.DTO.th;

import com.backend.portalroshkabackend.Models.Beneficios;
import com.backend.portalroshkabackend.Models.Enum.EstadoSolicitudEnum;
import com.backend.portalroshkabackend.Models.Permisos;
import com.backend.portalroshkabackend.Models.SolicitudThTipo;
import com.backend.portalroshkabackend.Models.Usuario;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.sql.Date;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class SolicitudTHResponseDto {

    private Integer idSolicitudTH;

    private String usuario;

    private String solicitudThTipo;

    private LocalDate fechaInicio;

    private Integer cantidadDias;

    private Date fechaCreacion;

    private EstadoSolicitudEnum estado;

}
