package com.backend.portalroshkabackend.DTO.Operationes;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

import com.backend.portalroshkabackend.Exception.ClienteExists;
// import com.backend.portalroshkabackend.Exception.UniqueNombre;
// import com.backend.portalroshkabackend.Exception.UniqueNombre;
import com.backend.portalroshkabackend.Exception.ValidEstado;
import com.backend.portalroshkabackend.Models.Clientes;
import com.backend.portalroshkabackend.Models.Usuario;

// @UniqueNombre   //для проверки по уникальности имени для create, для проверки уникальности Для update
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquiposRequestDto {

    private Integer idEquipo;

    @NotNull
    private Integer idLider;

    @NotBlank(message = "El nombre no puede estar vacio")
    private String nombre;

    @NotNull(message = "La fecha de inicio es obligatoria")
    private Date fechaInicio;

    @NotNull(message = "La fecha límite es obligatoria")
    private Date fechaLimite;

    @NotNull(message = "El ID del cliente es obligatorio")
    @ClienteExists
    private Integer idCliente;

    private Integer idTecnologia;

    @NotNull(message = "El estado es obligatorio")
    @ValidEstado
    private String estado;

}
