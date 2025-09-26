package com.backend.portalroshkabackend.DTO.Operationes.Metadatas;

import java.util.List;

import com.backend.portalroshkabackend.DTO.Operationes.UsuarioisResponseDto;

import lombok.Data;
@Data
public class MetaDatasDto {

    private List<TecnologiasResponseDto> tecnologias;
    private List<ClientesResponseDto> clientes;
    private List<UsuarioisResponseDto> teamLeaders;

    
}
