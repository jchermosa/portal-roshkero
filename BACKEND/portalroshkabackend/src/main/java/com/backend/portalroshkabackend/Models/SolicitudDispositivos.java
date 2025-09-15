package com.backend.portalroshkabackend.Models;

import java.sql.Date;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import com.backend.portalroshkabackend.Models.Enum.EstadoActivoInactivo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "solicitud_dispositivos")
public class SolicitudDispositivos {
    

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_solicitud_dispositivos")
    private Integer idSolicitudDispositivo;

    @Column(name = "fecha_inicio")
    private Date fechaInicio;

    @Column(name = "fecha_fin")
    private Date fechaFin;

    @Column(name = "cantidad_dias")
    private Integer cantidadDias;

    @Column(name = "aprobacion_admin")
    private Boolean aprobacionAdmin;

    @Column(name = "comentario")
    private String comentario;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", columnDefinition = "estado_ac_enum")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private EstadoActivoInactivo estado;

    @Column(name = "fecha_creacion")
    private Date fechaCreacion;

    @Column(name = "id_tipo_inventario")
    private Integer idTipoInventario;

    @Column(name = "id_usuario")
    private Integer idUsuario;
}
