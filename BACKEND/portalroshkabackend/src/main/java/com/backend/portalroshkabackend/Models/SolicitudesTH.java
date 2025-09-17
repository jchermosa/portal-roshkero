package com.backend.portalroshkabackend.Models;

import java.sql.Date;
import java.time.LocalDate;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.backend.portalroshkabackend.Models.Enum.EstadoSolicitudEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "solicitudes_th")
@Data
@NoArgsConstructor
public class SolicitudesTH {
    

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Column(name = "id_solicitud_th")
    private Integer idSolicitudTH;


    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin")
    private Date fechaFin;

    @JoinColumn(name = "id_usuario")
    @ManyToOne
    private Usuario usuario;

    @Column(name = "cantidad_dias")
    private Integer cantidadDias;

    @Column(name = "aprobacion_th")
    private Boolean aprobacionTH;
    
    @Column(name = "comentario")
    private String comentario;

    @ManyToOne
    @JoinColumn(name = "id_solicitud_th_tipo")
    private SolicitudThTipo solicitudThTipo;

    // Para enums PostgreSQL se debe especificar así y asegurar que los valores coinciden exactamente
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", columnDefinition = "estado_solicitud_enum")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private EstadoSolicitudEnum estado;

    @Column(name = "fecha_creacion")
    private Date fechaCreacion;

    @ManyToOne
    @JoinColumn(name = "id_permiso")
    private Permisos permisos;

    @ManyToOne
    @JoinColumn(name = "id_beneficio")
    private Beneficios beneficios;
}
