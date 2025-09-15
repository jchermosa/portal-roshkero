package com.backend.portalroshkabackend.Models;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
@Table(name = "asignacion_usuario")
public class AsignacionUsuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_asignacion")
    private Integer idAsignacionUsuario;

    @Column(name = "fecha_entrada")
    private Date fechaEntrada;

    @Column(name = "fecha_fin")
    private Date fechaFin;

    @Column(name ="porcenttaje_trabajo")
    private Float porcentajeTrabajo;

    @Column(name = "id_usuario")
    private Integer idUsuario;

    @Column(name = "id_tecnologia")
    private Integer idTecnologia;

    @Column(name = "equipos")
    private Integer equipos;

    @Column(name = "fecha_creacion")
    private Date fechaCreacion;
}
