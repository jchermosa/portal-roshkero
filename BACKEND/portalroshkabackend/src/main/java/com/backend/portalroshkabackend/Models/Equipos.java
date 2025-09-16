package com.backend.portalroshkabackend.Models;

import java.sql.Date;
import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "equipos")
public class Equipos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_equipo")
    private Integer idEquipo;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "fecha_inicio")
    private Date fechaInicio;

    @Column(name = "fecha_limite")
    private Date fechaLimite;

    @Column(name = "id_cliente")
    private int idCliente;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_cliente", insertable = false, updatable = false)
    private Clientes cliente;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoEquipo estado;

    public Equipos() {
    }

    public Equipos(String nombre) {
        this.nombre = nombre;
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

    public EstadoEquipo getEstado() {
        return estado;
    }

    public void setEstado(EstadoEquipo estado) {
        this.estado = estado;
    }

}
