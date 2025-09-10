package com.backend.portalroshkabackend.common.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;

@Entity
@Table(name = "solicitudes")
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_solicitud")
    private int idSolicitud;

    @Column(name="fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name="fecha_fin", nullable = false)
    private LocalDate fechaFin;

    @Column(name="estado", nullable = false)
    private Boolean estado;

    @Column(name="id_usuario", nullable = false)
    private int idUsuario;

    @Column(name="cantidad_dias", nullable = false)
    private int cantidadDias;

    @Column(name="numero_aprobaciones", nullable = false)
    private int numeroAprobaciones;

    @Column(name="comentario")
    private String comentario;

    @Column(name="rechazado", nullable = false)
    private Boolean rechazado;

    public int getId_solicitud() {
        return idSolicitud;
    }

    public void setId_solicitud(int idSolicitud) {
        this.idSolicitud = idSolicitud;
    }

    public LocalDate getFecha_inicio() {
        return fechaInicio;
    }

    public void setFecha_inicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFecha_fin() {
        return fechaFin;
    }

    public void setFecha_fin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public int getId_usuario() {
        return idUsuario;
    }

    public void setId_usuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getCantidad_dias() {
        return cantidadDias;
    }

    public void setCantidad_dias(int cantidadDias) {
        this.cantidadDias = cantidadDias;
    }

    public int getNumero_aprobaciones() {
        return numeroAprobaciones;
    }

    public void setNumero_aprobaciones(int numeroAprobaciones) {
        this.numeroAprobaciones = numeroAprobaciones;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Boolean getRechazado() {
        return rechazado;
    }

    public void setRechazado(Boolean rechazado) {
        this.rechazado = rechazado;
    }
}
