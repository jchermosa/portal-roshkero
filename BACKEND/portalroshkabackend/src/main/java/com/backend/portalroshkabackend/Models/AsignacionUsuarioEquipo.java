package com.backend.portalroshkabackend.Models;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.backend.portalroshkabackend.Models.Enum.EstadoActivoInactivo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;



@Entity
@NoArgsConstructor
@Data
@Table(name = "asignacion_usuario_equipo")
public class AsignacionUsuarioEquipo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_asignacion_usuario_equipo")
    private Integer idAsignacionUsuarioEquipo;

    @Column(name = "fecha_entrada")
    private LocalDate fechaEntrada;

    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    @Column(name ="porcentaje_trabajo")
    private int porcentajeTrabajo;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_equipo")
    private Equipos equipo;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", columnDefinition = "estado_ac_enum")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private EstadoActivoInactivo estado = EstadoActivoInactivo.A;

}
