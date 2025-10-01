package com.backend.portalroshkabackend.DTO.Operationes;

import java.sql.Date;
import java.time.LocalDateTime;

import com.backend.portalroshkabackend.Models.Clientes;

public class EquiposResponseDto {

    private Integer idEquipo;
    private String nombre;
    private Date fechaInicio;
    private Date fechaLimite;
    // private int idCliente;
    private Clientes cliente;
    private LocalDateTime fechaCreacion;
    private boolean estado;

    public EquiposResponseDto() {
    }

    public EquiposResponseDto(String nombre, Date fechaInicio, Date fechaLimite,
            Clientes cliente, LocalDateTime fechaCreacion, boolean estado) {
        this.nombre = nombre;
        this.fechaInicio = fechaInicio;
        this.fechaLimite = fechaLimite;
        // this.idCliente = idCliente;
        this.cliente = cliente;
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

    // public int getIdCliente() {
    //     return idCliente;
    // }

    // public void setIdCliente(int idCliente) {
    //     this.idCliente = idCliente;
    // }

    public Clientes getCliente() {
        return cliente;
    }

    public void setCliente(Clientes cliente) {
        this.cliente = cliente;
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
