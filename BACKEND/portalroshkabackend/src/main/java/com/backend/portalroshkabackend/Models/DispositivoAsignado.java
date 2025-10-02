package com.backend.portalroshkabackend.Models;

import java.time.LocalDate;
<<<<<<< HEAD
import java.time.LocalDateTime;
=======
>>>>>>> parent of dca61a3 (se elimino backend)

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
<<<<<<< HEAD
=======
import jakarta.persistence.OneToOne;
>>>>>>> parent of dca61a3 (se elimino backend)
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
<<<<<<< HEAD
@Table(name = "dispositivo_asignado")
=======
@Table(name = "dispositivos_asignados")
>>>>>>> parent of dca61a3 (se elimino backend)
public class DispositivoAsignado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_dispositivo_asignado")
    private Integer idDispositivoAsignado;

<<<<<<< HEAD
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
=======
    @Column(name = "fecha_entrega")
    private LocalDate fechaEntrega;

    @Column(name = "fecha_devolucion")
    private LocalDate fechaDevolucion;

    // Para enums PostgreSQL se debe especificar así y asegurar que los valores coinciden exactamente
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_asignacion", columnDefinition = "estado_asignacion_enum")
>>>>>>> parent of dca61a3 (se elimino backend)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private EstadoAsignacion estado;

    @ManyToOne
<<<<<<< HEAD
    @JoinColumn(name = "id_solicitud_dispositivos")
    private SolicitudDispositivos idSolicitudDispositivos;
=======
    @JoinColumn(name = "id_dispositivo")
    private Dispositivo dispositivo;

    @OneToOne
    @JoinColumn(name = "id_solicitud")
    private Solicitud solicitud;

    @Column(name = "observaciones")
    private String observaciones;

    
>>>>>>> parent of dca61a3 (se elimino backend)

}
