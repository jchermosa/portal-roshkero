package com.backend.portalroshkabackend.DTO.UsuarioDTO;



import com.backend.portalroshkabackend.Models.BeneficiosAsignados;
import com.backend.portalroshkabackend.Models.PermisosAsignados;
import com.backend.portalroshkabackend.Models.VacacionesAsignadas;
import com.backend.portalroshkabackend.Models.DispositivoAsignado;
import com.backend.portalroshkabackend.Models.Usuario;
import com.backend.portalroshkabackend.Models.Enum.EstadoSolicitudEnum;
import com.backend.portalroshkabackend.Models.Enum.SolicitudesEnum;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class SolicitudUserDto {

    private Integer idSolicitud;

    private Usuario usuario;

    private Usuario lider;

    private String nombreUsuario;

    private LocalDate fechaInicio;

    private LocalDate fechaFin;

    private Integer cantDias;

    private String comentario;

    private SolicitudesEnum tipoSolicitud;

    private EstadoSolicitudEnum estado;

    private LocalDateTime fechaCreacion;

    // Relaciones con asignaciones
    private PermisosAsignados permisoAsignado;
    private VacacionesAsignadas vacacionesAsignadas;
    private BeneficiosAsignados beneficioAsignado;
    private DispositivoAsignado dispositivoAsignado;

    // Puedes agregar campos adicionales según lo que necesites exponer en el DTO

}