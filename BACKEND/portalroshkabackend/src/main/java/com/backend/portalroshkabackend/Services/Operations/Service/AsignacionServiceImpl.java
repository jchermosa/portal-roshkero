package com.backend.portalroshkabackend.Services.Operations.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.backend.portalroshkabackend.DTO.Operationes.AsignacionResponseDto;
import com.backend.portalroshkabackend.DTO.Operationes.DiaConUbicacionesDto;
import com.backend.portalroshkabackend.DTO.Operationes.EquipoAsignacionUpdateDiasUbicacionesDto;
import com.backend.portalroshkabackend.DTO.Operationes.EquiposResponseDto;
import com.backend.portalroshkabackend.DTO.Operationes.UbicacionDiaDto;
import com.backend.portalroshkabackend.DTO.Operationes.UbicacionDto;
import com.backend.portalroshkabackend.DTO.Operationes.UsuarioisResponseDto;
import com.backend.portalroshkabackend.Models.DiaLaboral;
import com.backend.portalroshkabackend.Models.EquipoDiaUbicacion;
import com.backend.portalroshkabackend.Models.Ubicacion;
import com.backend.portalroshkabackend.Models.Equipos;
import com.backend.portalroshkabackend.Services.Operations.Interface.IAsignacionService;
import com.backend.portalroshkabackend.Repositories.OP.AsignacionUsuarioRepository;
import com.backend.portalroshkabackend.Repositories.OP.EquiposRepository;
import com.backend.portalroshkabackend.Repositories.AsignacionUbicacionDiaRepository;
import com.backend.portalroshkabackend.Repositories.UbicacionRepository;
import com.backend.portalroshkabackend.Repositories.DiasLaboralRepository;

@Service("operationsAsignacionService")
public class AsignacionServiceImpl implements IAsignacionService {
    @Autowired
    private AsignacionUsuarioRepository asignacionUsuarioRepository;
    @Autowired
    private AsignacionUbicacionDiaRepository asignacionUbicacionDiaRepository;
    @Autowired
    private EquiposRepository equiposRepository;
    @Autowired
    private DiasLaboralRepository diasLaboralRepository;
    @Autowired
    private UbicacionRepository ubicacionRepository;

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
    public List<DiaConUbicacionesDto> getDiasConUbicacionesLibres() {
        List<UbicacionDiaDto> results = asignacionUbicacionDiaRepository.findUbicacionesDiasLibresForEquipos();

        Map<Integer, DiaConUbicacionesDto> grouped = new HashMap<>();

        for (UbicacionDiaDto row : results) {
            grouped.computeIfAbsent(
                    row.getIdDiaLaboral(),
                    k -> new DiaConUbicacionesDto(row.getIdDiaLaboral(), row.getDia(), new ArrayList<>()))
                    .getUbicacionesLibres()
                    .add(new UbicacionDto(row.getIdUbicacion(), row.getNombre()));
        }

        return new ArrayList<>(grouped.values());
    }

    @Override
    public void asignarDiasUbicaciones(Integer idEquipo, EquipoAsignacionUpdateDiasUbicacionesDto request) {
        Equipos equipo = equiposRepository.findById(idEquipo)
                .orElseThrow(() -> new RuntimeException("Equipo no encontrado"));

        for (EquipoAsignacionUpdateDiasUbicacionesDto.DiaUbicacionDto dto : request.getAsignaciones()) {
            DiaLaboral dia = diasLaboralRepository.findById(dto.getIdDiaLaboral())
                    .orElseThrow(() -> new RuntimeException("Día no encontrado"));

            Optional<EquipoDiaUbicacion> existing = asignacionUbicacionDiaRepository.findByEquipoAndDiaLaboral(equipo,
                    dia);

            if (dto.getIdUbicacion() == null) {
                // Удаляем связь, если была
                existing.ifPresent(asignacionUbicacionDiaRepository::delete);
                continue;
            }

            // Создаём или обновляем
            EquipoDiaUbicacion asignacion = existing.orElseGet(() -> {
                EquipoDiaUbicacion nuevo = new EquipoDiaUbicacion();
                nuevo.setEquipo(equipo);
                nuevo.setDiaLaboral(dia);
                return nuevo;
            });

            Ubicacion ubicacion = ubicacionRepository.findById(dto.getIdUbicacion())
                    .orElseThrow(() -> new RuntimeException("Ubicación no encontrada"));
            asignacion.setUbicacion(ubicacion);

            asignacionUbicacionDiaRepository.save(asignacion);
        }
    }

}
