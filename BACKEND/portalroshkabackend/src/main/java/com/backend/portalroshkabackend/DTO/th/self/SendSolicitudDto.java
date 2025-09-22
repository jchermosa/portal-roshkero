package com.backend.portalroshkabackend.DTO.th.self;

import com.backend.portalroshkabackend.Models.BeneficiosAsignados;
import com.backend.portalroshkabackend.Models.PermisosAsignados;
import com.backend.portalroshkabackend.Models.Usuario;
import com.backend.portalroshkabackend.Models.Enum.SolicitudesEnum;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class SendSolicitudDto {

    private LocalDate fechaInicio;

    private Usuario usuario;

    private Integer cantidadDias;

    private String comentario;

    private SolicitudesEnum solicitudThTipo;

    private PermisosAsignados permisoAsignado;

    private BeneficiosAsignados beneficioAsignado;
}
