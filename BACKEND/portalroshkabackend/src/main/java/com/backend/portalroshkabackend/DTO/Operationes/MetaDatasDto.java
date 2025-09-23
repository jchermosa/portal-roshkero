package com.backend.portalroshkabackend.DTO.Operationes;

import java.util.List;
import lombok.Data;
@Data
public class MetaDatasDto {

    private List<TecnologiasResponseDto> tecnologias;
    private List<ClientesResponseDto> clientes;
    private List<UsuarioisResponseDto> teamLeaders;

    
}
