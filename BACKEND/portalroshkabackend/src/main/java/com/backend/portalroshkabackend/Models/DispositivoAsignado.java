package com.backend.portalroshkabackend.Models;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
@Table(name = "dispositivo_asignado")
public class DispositivoAsignado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_dispositivo_asignado")
    private Integer idDispositivoAsignado;

    @Column(name = "fecha_asignacion")
    private LocalDate fechaAsignacion;
    @Column(name = "fecha_devolucion")
    private LocalDate fechaDevolucion;
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    
    @ManyToOne
    @JoinColumn(name = "id_dispositivo")
    private TipoDispositivo idTipoDispositivo;

    // Para enums PostgreSQL se debe especificar así y asegurar que los valores coinciden exactamente
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", columnDefinition = "estado_asignacion_enum")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private EstadoAsignacion estado;

    @ManyToOne
    @JoinColumn(name = "id_solicitud_dispositivos")
    private SolicitudDispositivos idSolicitudDispositivos;

}
