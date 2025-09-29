package com.backend.portalroshkabackend.tools.mapper;

import org.springframework.stereotype.Component;

import com.backend.portalroshkabackend.DTO.Operationes.UsuarioisResponseDto;
import com.backend.portalroshkabackend.DTO.Operationes.Metadatas.ClientesResponseDto;
import com.backend.portalroshkabackend.DTO.Operationes.Tecnologias.TecnologiasResponseDto;
import com.backend.portalroshkabackend.Models.Clientes;
import com.backend.portalroshkabackend.Models.Tecnologias;
import com.backend.portalroshkabackend.Models.Usuario;

@Component
public class MetaDatasMapper {

    public ClientesResponseDto toClienteDto(Clientes cliente) {
        return new ClientesResponseDto(
                cliente.getIdCliente(),
                cliente.getNombre()
        );
    }

    public TecnologiasResponseDto toTecnologiaDto(Tecnologias tecnologia) {
        return new TecnologiasResponseDto(
                tecnologia.getIdTecnologia(),
                tecnologia.getNombre(),
                tecnologia.getDescripcion(),
                null //
        );
    }

    public UsuarioisResponseDto toUsuarioDto(Usuario usuario) {
        return new UsuarioisResponseDto(
                usuario.getIdUsuario(),
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getCorreo(),
                usuario.getDisponibilidad()
        );
    }
}

