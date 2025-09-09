package com.backend.portalroshkabackend.Models;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "usuarios")
public class Usuarios {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer idUsuario;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    @Column(unique = true, name = "nro_cedula", nullable = false)
    private Integer nroCedula;

    @Column(unique = true, nullable = false)
    private String correo;

    @Column(name = "id_rol", nullable = false)
    private Integer idRol;

    @Column(name = "fecha_ingreso", nullable = false)
    private LocalDate fechaIngreso;


    //    private Period antiguedad;
    @Column(name = "antiguedad")
    private String antiguedad;


    @Column(name = "dias_vacaciones")
    private Integer diasVacaciones = 0;

    private Boolean estado = true;

    @Column(nullable = false)
    private String contrasena = "default_password";

    private String telefono;

    @Column(name = "id_equipo")
    private Integer idEquipo;

    @Column(name = "id_cargo")
    private Integer idCargo;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Column(name = "dias_vacaciones_restante")
    private Integer diasVacacionesRestante = 0;

    @Column(name = "requiere_cambio_contrasena")
    private Boolean requiereCambioContrasena;

    // Getters y Setters

    // Constructor vacío
    public Usuarios() {}

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public Integer getNroCedula() {
        return nroCedula;
    }

    public void setNroCedula(Integer nroCedula) {
        this.nroCedula = nroCedula;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Integer getIdRol() {
        return idRol;
    }

    public void setIdRol(Integer idRol) {
        this.idRol = idRol;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public String getAntiguedad() {
        return antiguedad;
    }

    public void setAntiguedad(String antiguedad) {
        this.antiguedad = antiguedad;
    }

    public Integer getDiasVacaciones() {
        return diasVacaciones;
    }

    public void setDiasVacaciones(Integer diasVacaciones) {
        this.diasVacaciones = diasVacaciones;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Integer getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(Integer idEquipo) {
        this.idEquipo = idEquipo;
    }

    public Integer getIdCargo() {
        return idCargo;
    }

    public void setIdCargo(Integer idCargo) {
        this.idCargo = idCargo;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Integer getDiasVacacionesRestante() {
        return diasVacacionesRestante;
    }

    public void setDiasVacacionesRestante(Integer diasVacacionesRestante) {
        this.diasVacacionesRestante = diasVacacionesRestante;
    }

    public Boolean getRequiereCambioContrasena() {
        return requiereCambioContrasena;
    }

    public void setRequiereCambioContrasena(Boolean requiereCambioContrasena) {
        this.requiereCambioContrasena = requiereCambioContrasena;
    }
}


