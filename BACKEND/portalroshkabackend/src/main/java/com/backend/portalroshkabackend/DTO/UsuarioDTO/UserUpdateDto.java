package com.backend.portalroshkabackend.DTO.UsuarioDTO;

import java.time.LocalDate;
import com.backend.portalroshkabackend.Models.Enum.EstadoActivoInactivo;
import com.backend.portalroshkabackend.Models.Enum.SeniorityEnum;
import com.backend.portalroshkabackend.Models.Enum.FocoEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserUpdateDto {
    private String nombre;
    private String apellido;
    private String nroCedula;
    private String correo;
    private Integer idRol;
    private LocalDate fechaIngreso;
    private EstadoActivoInactivo estado;
    private String telefono;
    private Integer idCargo;
    private LocalDate fechaNacimiento;
    private Boolean requiereCambioContrasena;
    private SeniorityEnum seniority;
    private FocoEnum foco;
    private String urlPerfil;
    private Integer disponibilidad;
}