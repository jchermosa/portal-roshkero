package com.backend.portalroshkabackend.Models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import com.backend.portalroshkabackend.Models.Enum.EstadoActivoInactivo;
import com.backend.portalroshkabackend.Models.Enum.FocoEnum;
import com.backend.portalroshkabackend.Models.Enum.SeniorityEnum;

@Entity
@Data
@NoArgsConstructor
@Table(name = "usuarios")
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer idUsuario;
    



    @Column(name = "nombre", nullable = false)
    private String nombre;
    
    @Column(name = "apellido", nullable = false)
    private String apellido;
    
    @NotBlank
    @Column(name = "nro_cedula", unique = true, nullable = false)
    private String nroCedula;
    
    @Email
    @Column(name = "correo", unique = true, length = 320)
    private String correo;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_rol", nullable = false)
    private Roles rol;
    
    @Column(name = "fecha_ingreso", nullable = false)
    private LocalDate fechaIngreso;
    
    @Column(name = "antiguedad", insertable = false, updatable = false)
    private String antiguedad; // Se mapea como String para INTERVAL de PostgreSQL
    
    @Column(name = "dias_vacaciones", insertable = false, updatable = false)
    private Integer diasVacaciones = 0;
    
    @Column(name = "contrasena", nullable = false)
    private String contrasena = "default_password";
    
    @Column(name = "telefono", length = 20)
    private String telefono;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cargo")
    private Cargos cargo;
    
    @Past
    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;
    
    @Column(name = "dias_vacaciones_restante")
    private Integer diasVacacionesRestante = 0;
    
    @Column(name = "requiere_cambio_contrasena", nullable = false)
    private Boolean requiereCambioContrasena = true;
    
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "seniority", nullable = false, columnDefinition = "seniority_enum")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private SeniorityEnum seniority = SeniorityEnum.JUNIOR;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "foco", nullable = false, columnDefinition = "foco_enum")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private FocoEnum foco = FocoEnum.NINGUNO;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, columnDefinition = "estado_ac_enum")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private EstadoActivoInactivo estado = EstadoActivoInactivo.A;
    
    @Column(name = "url_perfil", length = 500)
    private String urlPerfil;
    
    @Min(0)
    @Max(100)
    @Column(name = "disponibilidad", nullable = false)
    private Integer disponibilidad = 100;
    
    // Relaciones
    // @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    // private List<Solicitud> solicitudes = new ArrayList<>();
    
    // @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
    // private List<AsignacionUsuarioEquipo> asignacionesEquipo = new ArrayList<>();
    
    // @OneToMany(mappedBy = "encargado", fetch = FetchType.LAZY)
    // private List<Dispositivo> dispositivosACargo = new ArrayList<>();

    // @OneToMany(mappedBy = "lider", fetch = FetchType.LAZY)
    // private List<Equipos> equiposLiderados = new ArrayList<>();

    // @OneToMany(mappedBy = "lider", fetch = FetchType.LAZY)
    // private List<Solicitud>  listaSolicitudes = new ArrayList<>();
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "tecnologias_usuario",
        joinColumns = @JoinColumn(name = "id_usuario"),
        inverseJoinColumns = @JoinColumn(name = "id_tecnologia")
    )
    private Set<Tecnologias> tecnologias = new HashSet<>();
    
}