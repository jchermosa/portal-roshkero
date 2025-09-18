package com.backend.portalroshkabackend.Models;

import java.sql.Date;

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

@Entity
@NoArgsConstructor
@Data
@Table(name = "asignacion_usuario_equipo")
public class AsignacionUsuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_asignacion_usuario_equipo")
    private Integer idAsignacionUsuarioEquipo;

    @Column(name = "fecha_entrada")
    private Date fechaEntrada;

    @Column(name = "fecha_fin")
    private Date fechaFin;

    @Column(name ="porcentaje_trabajo")
    private Float porcentajeTrabajo;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario idUsuario;

    @ManyToOne
    @JoinColumn(name = "id_tecnologia")
    private Tecnologias idTecnologia;

    @ManyToOne
    @JoinColumn(name = "id_equipo")
    private Equipos equipos;

    @Column(name = "fecha_creacion")
    private Date fechaCreacion;
}
