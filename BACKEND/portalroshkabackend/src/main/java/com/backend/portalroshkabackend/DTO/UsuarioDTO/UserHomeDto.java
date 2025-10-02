package com.backend.portalroshkabackend.DTO.UsuarioDTO;

import com.backend.portalroshkabackend.Models.Enum.EstadoActivoInactivo;
import com.backend.portalroshkabackend.Models.Enum.SeniorityEnum;
import com.backend.portalroshkabackend.Models.Enum.FocoEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
public class UserHomeDto {
    private Integer idUsuario;
    private String nombre;
    private String apellido;
    private String correo;
    private String nroCedula;
    private String telefono;
    private LocalDate fechaIngreso;
    private LocalDate fechaNacimiento;
    private EstadoActivoInactivo estado;
    private Boolean requiereCambioContrasena;
    private String antiguedad;
    private Integer diasVacaciones;
    private Integer diasVacacionesRestante;
    private Integer idRol;
    private String nombreRol;
    private Integer idCargo;
    private String nombreCargo;
    private SeniorityEnum seniority;
    private FocoEnum foco;
    private String urlPerfil;
    private Integer disponibilidad;
    private List<String> equipos; // Nombres de equipos a los que pertenece

    // Puedes agregar campos adicionales según lo que necesites exponer
}