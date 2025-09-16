package com.backend.portalroshkabackend.DTO.Operationes;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.sql.Date;

import com.backend.portalroshkabackend.Exception.ClienteExists;
import com.backend.portalroshkabackend.Exception.UniqueNombre;
import com.backend.portalroshkabackend.Exception.ValidEstado;
import com.backend.portalroshkabackend.Models.Clientes;
import com.backend.portalroshkabackend.Models.Enum.EstadoActivoInactivo;

public class EquiposRequestDto {

    @NotBlank(message = "El nombre no puede estar vacio")
    @UniqueNombre
    private String nombre;

    @NotNull(message = "La fecha de inicio es obligatoria")
    private Date fechaInicio;

    @NotNull(message = "La fecha límite es obligatoria")
    private Date fechaLimite;

    @NotNull(message = "El ID del cliente es obligatorio")
    @ClienteExists
    private Integer idCliente;

    @NotNull(message = "El estado es obligatorio")
    @ValidEstado
    private String estado;

    public EquiposRequestDto() {
    }

    public EquiposRequestDto(String nombre, Date fechaInicio, Date fechaLimite, Integer idCliente, String estado) {
        this.nombre = nombre;
        this.fechaInicio = fechaInicio;
        this.fechaLimite = fechaLimite;
        this.idCliente = idCliente;
        this.estado = estado;
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

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
