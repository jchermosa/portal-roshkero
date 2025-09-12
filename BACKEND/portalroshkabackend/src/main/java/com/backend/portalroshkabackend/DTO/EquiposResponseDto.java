package com.backend.portalroshkabackend.DTO;

import java.sql.Date;
import java.time.LocalDateTime;

public class EquiposResponseDto {

    private Integer idEquipo;
    private String nombre;
    private Date fechaInicio;
    private Date fechaLimite;
    private int idCliente;
    private LocalDateTime fechaCreacion;
    private boolean estado;

    public EquiposResponseDto() {
    }

    public EquiposResponseDto(String nombre, Date fechaInicio, Date fechaLimite,
            int idCliente, LocalDateTime fechaCreacion, boolean estado) {
        this.nombre = nombre;
        this.fechaInicio = fechaInicio;
        this.fechaLimite = fechaLimite;
        this.idCliente = idCliente;
        this.fechaCreacion = fechaCreacion;
        this.estado = estado;
    }

    public Integer getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(Integer idEquipo) {
        this.idEquipo = idEquipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaLimite() {
        return fechaLimite;
    }

    public void setFechaLimite(Date fechaLimite) {
        this.fechaLimite = fechaLimite;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
}
