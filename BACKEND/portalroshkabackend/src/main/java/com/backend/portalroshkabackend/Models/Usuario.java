package com.backend.portalroshkabackend.Models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.backend.portalroshkabackend.Models.Enum.EstadoActivoInactivo;

@Entity
@Data
@NoArgsConstructor
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer idUsuario;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "apellido")
    private String apellido;

    @Column(name = "nro_cedula")
    private int nroCedula;

    @Column(name = "correo")
    private String correo;

    @ManyToOne
    @JoinColumn(name = "id_rol",  nullable = false)
    private Roles roles;

    @Column(name = "fecha_ingreso")
    private LocalDate fechaIngreso;

    @Column(name = "antiguedad", insertable = false, updatable = false)
    private String antiguedad;

    @Column(name = "dias_vacaciones", insertable = false, updatable = false)
    private int diasVacaciones;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", columnDefinition = "estado_ac_enum")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private EstadoActivoInactivo estado;

    @Column(name = "contrasena")
    private String contrasena;

    @Column(name = "telefono")
    private String telefono;

    @ManyToOne
    @JoinColumn(name = "id_cargo",  nullable = false)
    private Cargos cargos;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Column(name = "dias_vacaciones_restante")
    private int diasVacacionesRestante;

    @Column(name = "requiere_cambio_contrasena")
    private boolean requiereCambioContrasena;

    @Column(name = "url_perfil")
    private String url;

    @Column(name = "disponibilidad")
    private Integer disponibilidad;

}
