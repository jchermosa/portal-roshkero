package com.backend.portalroshkabackend.Models;

import jakarta.persistence.*;

import java.sql.Date;
import java.time.LocalDateTime;

@Entity
@Table(name = "solicitudes")
public class Solicitudes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_solicitud")
    private int idSolicitud;

    @Column(name = "fecha_inicio")
    private Date fechaInicio;

    @Column(name = "fecha_fin")
    private Date fechaFin;

    @Column(name = "id_usuario")
    private int idUsuario;

    @Column(name = "cantidad_dias")
    private int cantidadDias;

    @Column(name = "numero_aprobaciones")
    private int numeroAprobaciones;

    @Column(name = "comentario")
    private String comentario;

    @Column(name = "id_solicitud_tipo")
    private int idSolicitudTipo;

    @Enumerated(EnumType.STRING) // string enum
    @Column(name = "estado")
    private EstadoSolicitud estado;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    public int getIdSolicitud() {
        return idSolicitud;
    }

    public void setIdSolicitud(int idSolicitud) {
        this.idSolicitud = idSolicitud;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public EstadoSolicitud getEstado() {
        return estado;
    }

    public void setEstado(EstadoSolicitud estado) {
        this.estado = estado;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getCantidadDias() {
        return cantidadDias;
    }

    public void setCantidadDias(int cantidadDias) {
        this.cantidadDias = cantidadDias;
    }

    public int getNumeroAprobaciones() {
        return numeroAprobaciones;
    }

    public void setNumeroAprobaciones(int numeroAprobaciones) {
        this.numeroAprobaciones = numeroAprobaciones;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public int getidSolicitudTipo() {
        return idSolicitudTipo;
    }

    public void setidSolicitudTipo(int idSolicitudTipo) {
        this.idSolicitudTipo = idSolicitudTipo;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

}
