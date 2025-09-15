package com.backend.portalroshkabackend.Models;

import java.sql.Date;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.backend.portalroshkabackend.Models.Enum.EstadoAsignacion;

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
import lombok.Data;

@Entity
@Data
@Table(name = "inventario_asignado")
public class InventarioAsignado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_asignacion")
    private Integer idAsignacion;

    @Column(name = "fecha_asignacion")
    private Date fechaAsignacion;
    @Column(name = "fecha_devolucion")
    private Date fechaDevolucion;
    @Column(name = "fecha_creacion")
    private Date fechaCreacion;

    
    @ManyToOne
    @JoinColumn(name = "id_tipo_inventario")
    private TipoInventario idInventario;

    // Para enums PostgreSQL se debe especificar así y asegurar que los valores coinciden exactamente
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", columnDefinition = "estado_asignacion_enum")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private EstadoAsignacion estado;

    @ManyToOne
    @JoinColumn(name = "id_solicitud_dispositivos")
    private SolicitudDispositivos idSolicitudDispositivos;

}
