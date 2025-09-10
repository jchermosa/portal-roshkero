package com.backend.portalroshkabackend.admin.dto;

import java.sql.Date;

public class RequestDto {
    private int idSolicitud;
    private Date fechaInicio;
    private Date fechaFin;
    private boolean estado;
    private int idUsuario;
    private int cantidadDias;
    private int numeroAprobaciones;
    private String comentario;
    private boolean rechazado;

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

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
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

    public boolean isRechazado() {
        return rechazado;
    }

    public void setRechazado(boolean rechazado) {
        this.rechazado = rechazado;
    }
}
