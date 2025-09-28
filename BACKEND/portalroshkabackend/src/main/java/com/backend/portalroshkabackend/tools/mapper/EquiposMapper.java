package com.backend.portalroshkabackend.tools.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.backend.portalroshkabackend.DTO.Operationes.EquipoDiaUbicacionResponceDto;
import com.backend.portalroshkabackend.DTO.Operationes.EquiposResponseDto;
import com.backend.portalroshkabackend.DTO.Operationes.TecnologiasDto;
import com.backend.portalroshkabackend.DTO.Operationes.UsuarioisResponseDto;
import com.backend.portalroshkabackend.DTO.Operationes.Metadatas.ClientesResponseDto;
import com.backend.portalroshkabackend.Models.Equipos;
import com.backend.portalroshkabackend.Models.Usuario;
import com.backend.portalroshkabackend.Services.Operations.Interface.ITecnologiaEquiposService;
import com.backend.portalroshkabackend.Services.Operations.Interface.AsignacionUsuarioEquipo.IAsignacionUsuarioEquipoService;
import com.backend.portalroshkabackend.Services.Operations.Interface.Equipo.IEquipoDiaUbicacionService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EquiposMapper {

    private final IEquipoDiaUbicacionService equipoDiaUbicacionService;
    private final ITecnologiaEquiposService tecnologiaEquiposService;
    private final IAsignacionUsuarioEquipoService asignacionUsuarioEquipoService;

    public EquiposResponseDto getTeam(Equipos e) {
        EquiposResponseDto dto = new EquiposResponseDto();
        dto.setIdEquipo(e.getIdEquipo());
        dto.setNombre(e.getNombre());
        dto.setFechaInicio(e.getFechaInicio());
        dto.setFechaLimite(e.getFechaLimite());
        dto.setFechaCreacion(e.getFechaCreacion());
        dto.setEstado(e.getEstado());

        dto.setEquipoDiaUbicacion(equipoDiaUbicacionService.EquipoDiaUbicacion(e.getIdEquipo()));
        dto.setTecnologias(tecnologiaEquiposService.getTecnologiasByEquipo(e.getIdEquipo()));
        dto.setUsuariosAsignacion(asignacionUsuarioEquipoService.getUsuariosAsignacion(e.getIdEquipo()));
        dto.setUsuariosNoEnEquipo(asignacionUsuarioEquipoService.getUsuariosFueraEquipo(e.getIdEquipo()));

        if (e.getLider() != null) {
            dto.setLider(new UsuarioisResponseDto(
                    e.getLider().getIdUsuario(),
                    e.getLider().getNombre(),
                    e.getLider().getApellido(),
                    e.getLider().getCorreo(),
                    e.getLider().getDisponibilidad()));
        }

        if (e.getCliente() != null) {
            dto.setCliente(new ClientesResponseDto(
                    e.getCliente().getIdCliente(),
                    e.getCliente().getNombre()));
        }

        return dto;
    }

    public EquiposResponseDto getAllTeams(Equipos e) {
        EquiposResponseDto dto = new EquiposResponseDto();
        dto.setIdEquipo(e.getIdEquipo());
        dto.setNombre(e.getNombre());

        // Ubicaciones
        List<EquipoDiaUbicacionResponceDto> dtoList = equipoDiaUbicacionService
                .EquipoDiaUbicacion(e.getIdEquipo());
        dto.setEquipoDiaUbicacion(dtoList);

        // Fechas y estado
        dto.setFechaInicio(e.getFechaInicio());
        dto.setFechaLimite(e.getFechaLimite());
        dto.setFechaCreacion(e.getFechaCreacion());
        dto.setEstado(e.getEstado());

        // Lider
        if (e.getLider() != null) {
            Usuario u = e.getLider();
            dto.setLider(new UsuarioisResponseDto(
                    u.getIdUsuario(),
                    u.getNombre(),
                    u.getApellido(),
                    u.getCorreo(),
                    u.getDisponibilidad()));
        }

        // Cliente
        if (e.getCliente() != null) {
            dto.setCliente(new ClientesResponseDto(
                    e.getCliente().getIdCliente(),
                    e.getCliente().getNombre()));
        }

        // Tecnologias
        List<TecnologiasDto> tecnologias = tecnologiaEquiposService.getTecnologiasByEquipo(e.getIdEquipo());
        dto.setTecnologias(tecnologias);

        return dto;
    }
}