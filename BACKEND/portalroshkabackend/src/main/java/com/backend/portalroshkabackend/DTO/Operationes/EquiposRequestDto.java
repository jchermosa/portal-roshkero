package com.backend.portalroshkabackend.DTO.Operationes;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Data;
import java.sql.Date;

import com.backend.portalroshkabackend.Exception.ClienteExists;
// import com.backend.portalroshkabackend.Exception.UniqueNombre;
// import com.backend.portalroshkabackend.Exception.UniqueNombre;
import com.backend.portalroshkabackend.Exception.ValidEstado;
import com.backend.portalroshkabackend.Models.Clientes;

@Data
// @UniqueNombre   //для проверки по уникальности имени для create, для проверки уникальности Для update
public class EquiposRequestDto {

    private Integer idEquipo;

    @NotBlank(message = "El nombre no puede estar vacio")
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
}
