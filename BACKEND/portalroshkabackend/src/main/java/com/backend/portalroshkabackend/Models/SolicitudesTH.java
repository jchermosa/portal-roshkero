package com.backend.portalroshkabackend.Models;

import java.sql.Date;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.backend.portalroshkabackend.Models.Enum.EstadoSolicitudEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "solicitudes_th")
@Data
@NoArgsConstructor
public class SolicitudesTH {
    

    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Column(name = "id_solicitud")
    private Integer idSolicitudTH;


    @Column(name = "fecha_inicio")
    private Date fechaInicio;

    @Column(name = "fecha_fin")
    private Date fechaFin;

    @Column(name = "id_usuario")
    private Integer idUsuario;

    @Column(name = "cantidad_dias")
    private Integer cantidadDias;

    @Column(name = "aprobacion_th")
    private Boolean aprobacionTH;
    
    @Column(name = "comentario")
    private String comentario;

    @Column(name = "id_solicitud_tipo")
    private Integer idSolicitudTipo;

    // Para enums PostgreSQL se debe especificar así y asegurar que los valores coinciden exactamente
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", columnDefinition = "estado_solicitud_enum")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private EstadoSolicitudEnum estado;

    @Column(name = "fecha_creacion")
    private Date fechaCreacion;

    @Column(name = "id_permiso")
    private Integer idPermiso;

    @Column(name = "id_beneficio")
    private Integer idBeneficio;
}
