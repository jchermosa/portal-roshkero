package com.backend.portalroshkabackend.Models;

import java.time.LocalDate;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;


import com.backend.portalroshkabackend.Models.Enum.EstadoSolicitudEnum;
import com.backend.portalroshkabackend.Models.Enum.SolicitudesEnum;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@Entity
@Table(name = "solicitudes")
public class Solicitud {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_solicitud")
    private Integer idSolicitud;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = true)
    private Usuario teamLeader;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_documento_adjunto")
    private DocumentoAdjunto documentoAdjunto;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_solicitud", nullable = false, columnDefinition = "solicitudes_enum")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private SolicitudesEnum tipoSolicitud ;

    @Column(name = "comentario")
    private String comentario;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, columnDefinition = "estado_solicitud_enum")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private EstadoSolicitudEnum estado = EstadoSolicitudEnum.P;

    @OneToOne(mappedBy = "solicitud", cascade = CascadeType.ALL)
    private PermisosAsignados permisoAsignado;

    @OneToOne(mappedBy = "solicitud", cascade = CascadeType.ALL)
    private VacacionesAsignadas vacacionesAsignadas;

    @OneToOne(mappedBy = "solicitud", cascade = CascadeType.ALL)
    private BeneficiosAsignados beneficioAsignado;

    @OneToOne(mappedBy = "solicitud", cascade = CascadeType.ALL)
    private DispositivoAsignado dispositivoAsignado;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "cant_dias")
    private Integer cantDias;

    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;


}
