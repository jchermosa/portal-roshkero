package com.backend.portalroshkabackend.Models;

import java.sql.Date;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import com.backend.portalroshkabackend.Models.Enum.EstadoActivoInactivo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "equipos")
public class Equipos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_equipo")
    private Integer idEquipo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_lider", referencedColumnName = "id_usuario")
    private Usuario lider;

    private String nombre;

    // @ManyToOne
    // @JoinColumn(name = "id_cliente")
    // private Clientes idCliente;

    @Column(name = "fecha_inicio")
    private Date fechaInicio;
    @Column(name = "fecha_limite")
    private Date fechaLimite;
    @Column(name = "fecha_creacion")
    private Date fechaCreacion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_cliente")
    private Clientes cliente;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", columnDefinition = "estado_ac_enum")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private EstadoActivoInactivo estado;

}
