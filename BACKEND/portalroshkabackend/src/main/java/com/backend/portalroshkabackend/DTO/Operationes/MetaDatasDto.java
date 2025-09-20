package com.backend.portalroshkabackend.DTO.Operationes;

import java.util.List;
import lombok.Data;

import com.backend.portalroshkabackend.DTO.Operationes.TecnologiasResponseDto;
import com.backend.portalroshkabackend.DTO.Operationes.ClientesResponseDto;
import com.backend.portalroshkabackend.DTO.Operationes.UsuarioisResponseDto;

@Data
public class MetaDatasDto {

    private List<TecnologiasResponseDto> tecnologias;
    private List<ClientesResponseDto> clientes;
    private List<UsuarioisResponseDto> teamLeaders;

    
}
