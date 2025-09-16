package com.backend.portalroshkabackend.DTO.Operationes;

import java.sql.Date;
import java.time.LocalDateTime;

import com.backend.portalroshkabackend.Models.Clientes;
import com.backend.portalroshkabackend.Models.Enum.EstadoActivoInactivo;

public class EquiposResponseDto {

    private Integer idEquipo;
    private String nombre;
    private Date fechaInicio;
    private Date fechaLimite;
    private Clientes cliente;
    private Date fechaCreacion;
    private EstadoActivoInactivo estado;

    public EquiposResponseDto() {
    }

    public EquiposResponseDto(String nombre, Date fechaInicio, Date fechaLimite,
            Clientes cliente, Date fechaCreacion, EstadoActivoInactivo estado) {
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

    public Clientes getCliente() {
        return cliente;
    }

    public void setCliente(Clientes cliente) {
        this.cliente = cliente;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public EstadoActivoInactivo getEstado() {
        return estado;
    }

    public void setEstado(EstadoActivoInactivo estado) {
        this.estado = estado;
    }
}
