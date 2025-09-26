package com.backend.portalroshkabackend.Services.Operations.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.backend.portalroshkabackend.DTO.Operationes.AsignacionResponseDto;
import com.backend.portalroshkabackend.DTO.Operationes.EquiposResponseDto;
import com.backend.portalroshkabackend.DTO.Operationes.UbicacionDiaDto;
import com.backend.portalroshkabackend.DTO.Operationes.UsuarioisResponseDto;
import com.backend.portalroshkabackend.DTO.Operationes.Tecnologias.TecnologiasResponseDto;
import com.backend.portalroshkabackend.Services.Operations.Interface.IAsignacionService;
import com.backend.portalroshkabackend.Repositories.AsignacionUbicacionDiaRepository;
import com.backend.portalroshkabackend.Repositories.AsignacionUsuarioRepository;

@Service("operationsAsignacionService")
public class AsignacionServiceImpl implements IAsignacionService {
    @Autowired
    private AsignacionUsuarioRepository asignacionUsuarioRepository;
    @Autowired
    private AsignacionUbicacionDiaRepository asignacionUbicacionDiaRepository;

    @Override
    public Page<AsignacionResponseDto> getAllAsignacion(Pageable pageable) {
        return asignacionUsuarioRepository.findAll(pageable)
                .map(asignacion -> {
                    AsignacionResponseDto dto = new AsignacionResponseDto();

                    // --- Основные поля ---
                    dto.setIdAsignacionUsuarioEquipo(asignacion.getIdAsignacionUsuarioEquipo());
                    dto.setFechaEntrada(asignacion.getFechaEntrada());
                    dto.setFechaFin(asignacion.getFechaFin());
                    dto.setPorcentajeTrabajo(asignacion.getPorcentajeTrabajo());
                    dto.setFechaCreacion(asignacion.getFechaCreacion());

                    // --- Usuario ---
                    if (asignacion.getUsuario() != null) {
                        UsuarioisResponseDto usuarioDto = new UsuarioisResponseDto();
                        usuarioDto.setIdUsuario(asignacion.getUsuario().getIdUsuario());
                        usuarioDto.setNombre(asignacion.getUsuario().getNombre());
                        usuarioDto.setApellido(asignacion.getUsuario().getApellido());
                        usuarioDto.setCorreo(asignacion.getUsuario().getCorreo());
                        usuarioDto.setDisponibilidad(asignacion.getUsuario().getDisponibilidad());
                        dto.setUsuario(usuarioDto);
                    }

                    // --- Equipo ---
                    if (asignacion.getEquipo() != null) {
                        EquiposResponseDto equipoDto = new EquiposResponseDto();
                        equipoDto.setIdEquipo(asignacion.getEquipo().getIdEquipo());
                        equipoDto.setNombre(asignacion.getEquipo().getNombre());
                        dto.setEquipo(equipoDto);
                    }

                    return dto;
                });
    }

    @Override
    public List<UbicacionDiaDto> getUbicacionesDiasLibresForEquipo(Integer idEquipo) {
        return asignacionUbicacionDiaRepository.findUbicacionesDiasLibresForEquipo(idEquipo)
                .stream()
                .map(row -> new UbicacionDiaDto(
                        (Integer) row[0],
                        (String) row[1],
                        (Integer) row[2],
                        (String) row[3]))
                .toList();
    }

}
