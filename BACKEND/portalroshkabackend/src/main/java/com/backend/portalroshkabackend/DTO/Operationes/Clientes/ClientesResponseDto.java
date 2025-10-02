package com.backend.portalroshkabackend.DTO.Operationes.Clientes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientesResponseDto {
    private Integer idCliente;
    private String nombre;
    private String nroTelefono;
    private String correo;
    private String ruc;
    private LocalDateTime fechaCreacion;
}   