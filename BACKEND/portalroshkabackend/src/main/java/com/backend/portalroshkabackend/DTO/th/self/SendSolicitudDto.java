package com.backend.portalroshkabackend.DTO.th.self;

import com.backend.portalroshkabackend.Models.Beneficios;

import com.backend.portalroshkabackend.Models.Permisos;
import com.backend.portalroshkabackend.Models.SolicitudThTipo;
import com.backend.portalroshkabackend.Models.Usuario;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.sql.Date;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class SendSolicitudDto {

    private LocalDate fechaInicio;

    private Usuario usuario;

    private Integer cantidadDias;

    private String comentario;

    private SolicitudThTipo solicitudThTipo;

    private Permisos permisos;

    private Beneficios beneficios;
}
