package com.backend.portalroshkabackend.Models;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.Period;

@Entity
@Data
@Table(name = "usuarios")
public class usuarios {
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


    public usuarios(Integer idUsuario, String nombre, String apellido, Integer nroCedula, String correo, Integer idRol,
                    LocalDate fechaIngreso, String antiguedad, Integer diasVacaciones, Boolean estado, String contrasena,
                    String telefono, Integer idEquipo, Integer idCargo, LocalDate fechaNacimiento, Integer diasVacacionesRestante,
                    Boolean requiereCambioContrasena) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.apellido = apellido;
        this.nroCedula = nroCedula;
        this.correo = correo;
        this.idRol = idRol;
        this.fechaIngreso = fechaIngreso;
        this.antiguedad = antiguedad;
        this.diasVacaciones = diasVacaciones;
        this.estado = estado;
        this.contrasena = contrasena;
        this.telefono = telefono;
        this.idEquipo = idEquipo;
        this.idCargo = idCargo;
        this.fechaNacimiento = fechaNacimiento;
        this.diasVacacionesRestante = diasVacacionesRestante;
        this.requiereCambioContrasena = requiereCambioContrasena;
    }

    public usuarios() {

    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public Integer getNroCedula() {
        return nroCedula;
    }

    public String getCorreo() {
        return correo;
    }

    public Integer getIdRol() {
        return idRol;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public String getAntiguedad() {
        return antiguedad;
    }

    public Integer getDiasVacaciones() {
        return diasVacaciones;
    }

    public Boolean getEstado() {
        return estado;
    }

    public String getContrasena() {
        return contrasena;
    }

    public String getTelefono() {
        return telefono;
    }

    public Integer getIdEquipo() {
        return idEquipo;
    }

    public Integer getIdCargo() {
        return idCargo;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public Integer getDiasVacacionesRestante() {
        return diasVacacionesRestante;
    }

    public Boolean getRequiereCambioContrasena() {
        return requiereCambioContrasena;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setNroCedula(Integer nroCedula) {
        this.nroCedula = nroCedula;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public void setIdRol(Integer idRol) {
        this.idRol = idRol;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;

        calcularAntiguedad(fechaIngreso);
    }

    public void setAntiguedad(String antiguedad) {
        this.antiguedad = antiguedad;
    }

    public void setDiasVacaciones(Integer diasVacaciones) {
        this.diasVacaciones = diasVacaciones;
        calcularDiasVacacionesRestantes(diasVacaciones);
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setIdEquipo(Integer idEquipo) {
        this.idEquipo = idEquipo;
    }

    public void setIdCargo(Integer idCargo) {
        this.idCargo = idCargo;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public void setDiasVacacionesRestante(Integer diasVacacionesRestante) {
        this.diasVacacionesRestante = diasVacacionesRestante;
    }

    public void setRequiereCambioContrasena(Boolean requiereCambioContrasena) {
        this.requiereCambioContrasena = requiereCambioContrasena;
    }

    public void calcularDiasVacacionesRestantes(int diasVacas){
        setDiasVacacionesRestante(Math.max(30 - diasVacas, 0));
    }

    private void calcularAntiguedad(LocalDate fechaIngreso) {
        if (fechaIngreso == null) {
            setAntiguedad("0 años 0 meses 0 días");
            return;
        }

        LocalDate hoy = LocalDate.now();

        // Calculamos el período entre la fecha de ingreso y hoy
        Period periodo = Period.between(fechaIngreso, hoy);

        // Formateamos el periodo a un string legible
        setAntiguedad(formatPeriod(periodo));
    }

    //Metodo auxiliar para convertir Period a texto legible
    private String formatPeriod(Period periodo) {
        StringBuilder sb = new StringBuilder();

        if (periodo.getYears() > 0) {
            sb.append(periodo.getYears()).append(" año");
            if (periodo.getYears() > 1) sb.append("s");
            sb.append(" ");
        }

        if (periodo.getMonths() > 0) {
            sb.append(periodo.getMonths()).append(" mes");
            if (periodo.getMonths() > 1) sb.append("es");
            sb.append(" ");
        }

        if (periodo.getDays() > 0 || sb.isEmpty()) { // mostrar días si no hay años ni meses
            sb.append(periodo.getDays()).append(" día");
            if (periodo.getDays() != 1) sb.append("s");
        }

        return sb.toString().trim();
    }

}
