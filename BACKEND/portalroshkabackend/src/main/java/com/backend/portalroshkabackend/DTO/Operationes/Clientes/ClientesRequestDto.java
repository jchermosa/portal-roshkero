package com.backend.portalroshkabackend.DTO.Operationes.Clientes;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ClientesRequestDto {
    private String nombre;
    private String nroTelefono;
    private String correo;
    private String ruc;
}

