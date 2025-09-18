package com.backend.portalroshkabackend.Models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.backend.portalroshkabackend.Models.Enum.EstadoSolicitudEnum;

@Entity
@Data
@NoArgsConstructor
@Table(name = "solicitudes")
public class Solicitudes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_solicitud")
    private int idSolicitud;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", columnDefinition = "estado_sol_enum")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private EstadoSolicitudEnum estado;

    @Column(name = "id_usuario")
    private int idUsuario;

    @Column(name = "cantidad_dias")
    private int cantidadDias;

    @Column(name = "numero_aprobaciones")
    private int numeroAprobaciones;

    @Column(name = "comentario")
    private String comentario;

    @Column(name = "rechazado")
    private boolean rechazado;

}
