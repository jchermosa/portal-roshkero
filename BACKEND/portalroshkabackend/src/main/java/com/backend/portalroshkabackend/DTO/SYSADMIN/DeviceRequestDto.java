package com.backend.portalroshkabackend.DTO.SYSADMIN;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Date;
import java.time.LocalDate;

import com.backend.portalroshkabackend.Models.Enum.EstadoSolicitudEnum;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Data
@NoArgsConstructor
public class DeviceRequestDto {
   
 
    @NotNull ( message = "El tipo de dispositivo es Obligatorio")
    @Positive ( message = "El id debe ser positivo")
    private int idTipoDispositivo;

    private String comentario;

    
    @NotNull ( message = "El usuario que realiza la solicitud es Obligatorio")
    @Positive ( message = "El id debe ser positivo")
    private int idUsuario;

    
    private String nombreUsuario;



}
