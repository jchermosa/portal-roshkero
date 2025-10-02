package com.backend.portalroshkabackend.DTO.th.request;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.backend.portalroshkabackend.Models.Enum.EstadoSolicitudEnum;

public class RequestDto {
    private int idSolicitud;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private EstadoSolicitudEnum estado;
    private int idUsuario;
    private int cantidadDias;
    private int numeroAprobaciones;
    private String comentario;
    private int idSolicitudTipo;
    private LocalDateTime fechaCreacion;

    public int getIdSolicitud() {
        return idSolicitud;
    }

    public void setIdSolicitud(int idSolicitud) {
        this.idSolicitud = idSolicitud;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public EstadoSolicitudEnum getEstado() {
        return estado;
    }

    public void setEstado(EstadoSolicitudEnum estado) {
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
    
    public int getidSolicitudTipo() {
        return idSolicitudTipo;
    }

    public void setidSolicitudTipo(int idSolicitudTipo) {
        this.idSolicitudTipo = idSolicitudTipo;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
