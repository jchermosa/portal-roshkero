package com.backend.portalroshkabackend.DTO.Operationes;

import lombok.Data;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientesResponseDto {

    private Integer idCliente;
    private String nombre;

}
