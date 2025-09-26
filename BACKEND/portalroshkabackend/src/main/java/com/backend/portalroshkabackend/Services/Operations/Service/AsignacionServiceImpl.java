package com.backend.portalroshkabackend.Services.Operations.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.backend.portalroshkabackend.DTO.Operationes.AsignacionResponseDto;
import com.backend.portalroshkabackend.DTO.Operationes.DiasLaboralDto;
import com.backend.portalroshkabackend.DTO.Operationes.EquiposResponseDto;
import com.backend.portalroshkabackend.DTO.Operationes.UbicacionConDiasDto;
import com.backend.portalroshkabackend.DTO.Operationes.UbicacionDto;
import com.backend.portalroshkabackend.DTO.Operationes.UsuarioisResponseDto;
import com.backend.portalroshkabackend.Services.Operations.Interface.IAsignacionService;
import com.backend.portalroshkabackend.Repositories.AsignacionUbicacionDiaRepository;

@Service("operationsAsignacionService")
public class AsignacionServiceImpl implements IAsignacionService {
    @Autowired
    private com.backend.portalroshkabackend.Repositories.OP.AsignacionUsuarioRepository asignacionUsuarioRepository;
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
    public List<UbicacionConDiasDto> getUbicacionesConDiasLibres() {

        List<UbicacionDto> results = asignacionUbicacionDiaRepository.findUbicacionesDiasLibresForEquipos();

        Map<Integer, UbicacionConDiasDto> grouped = new HashMap<>();

        for (UbicacionDto row : results) {
            grouped.computeIfAbsent(
                    row.getIdUbicacion(),
                    k -> new UbicacionConDiasDto(row.getIdUbicacion(), row.getNombre(), new ArrayList<>()))
                    .getDiasLibres()
                    .add(new DiasLaboralDto(row.getIdDiaLaboral(), row.getDia()));
        }

        return new ArrayList<>(grouped.values());
    }
}
