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
import com.backend.portalroshkabackend.DTO.Operationes.DiaUbicacionDto;
import com.backend.portalroshkabackend.DTO.Operationes.EquiposResponseDto;
import com.backend.portalroshkabackend.DTO.Operationes.UbicacionDiaDto;
import com.backend.portalroshkabackend.DTO.Operationes.UbicacionDto;
import com.backend.portalroshkabackend.DTO.Operationes.UsuarioisResponseDto;
import com.backend.portalroshkabackend.Models.AsignacionUsuarioEquipo;
import com.backend.portalroshkabackend.Models.DiaLaboral;
import com.backend.portalroshkabackend.Models.EquipoDiaUbicacion;
import com.backend.portalroshkabackend.Models.Ubicacion;
import com.backend.portalroshkabackend.Models.Usuario;
import com.backend.portalroshkabackend.Models.Enum.EstadoActivoInactivo;
import com.backend.portalroshkabackend.Models.Equipos;
import com.backend.portalroshkabackend.Services.Operations.Interface.IAsignacionService;

import jakarta.transaction.Transactional;

import com.backend.portalroshkabackend.Repositories.OP.AsignacionUsuarioRepository;
import com.backend.portalroshkabackend.Repositories.OP.EquiposRepository;
import com.backend.portalroshkabackend.Repositories.OP.UsuarioisRepository;
import com.backend.portalroshkabackend.Repositories.AsignacionUbicacionDiaRepository;
import com.backend.portalroshkabackend.Repositories.UbicacionRepository;
import com.backend.portalroshkabackend.Repositories.DiasLaboralRepository;

@Service("operationsAsignacionService")
public class AsignacionServiceImpl implements IAsignacionService {

    private final UsuarioisRepository usuarioisRepository;
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

    AsignacionServiceImpl(UsuarioisRepository usuarioisRepository) {
        this.usuarioisRepository = usuarioisRepository;
    }

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
    public void asignarDiasUbicacionesEquipo(Integer idEquipo, List<DiaUbicacionDto> diasUbicacionesEquipo) {
        // Получаем команду
        Equipos equipo = equiposRepository.findById(idEquipo)
                .orElseThrow(() -> new RuntimeException("Equipo no encontrado"));

        if (diasUbicacionesEquipo == null || diasUbicacionesEquipo.isEmpty()) {
            return; // если список пустой или null — выходим
        }

        for (DiaUbicacionDto dto : diasUbicacionesEquipo) {
            // Находим день
            DiaLaboral dia = diasLaboralRepository.findById(dto.getIdDiaLaboral())
                    .orElseThrow(() -> new RuntimeException("Día no encontrado"));

            // Проверяем, есть ли уже назначение для этой команды и дня
            Optional<EquipoDiaUbicacion> existing = asignacionUbicacionDiaRepository.findByEquipoAndDiaLaboral(equipo,
                    dia);

            if (dto.getIdUbicacion() == null) {
                // Если idUbicacion == null → удаляем существующую связь
                existing.ifPresent(asignacionUbicacionDiaRepository::delete);
                continue;
            }

            // Создаём или обновляем запись
            EquipoDiaUbicacion asignacion = existing.orElseGet(() -> {
                EquipoDiaUbicacion nuevo = new EquipoDiaUbicacion();
                nuevo.setEquipo(equipo);
                nuevo.setDiaLaboral(dia);
                return nuevo;
            });

            // Находим локацию
            Ubicacion ubicacion = ubicacionRepository.findById(dto.getIdUbicacion())
                    .orElseThrow(() -> new RuntimeException("Ubicación no encontrada"));

            asignacion.setUbicacion(ubicacion);

            // Сохраняем
            asignacionUbicacionDiaRepository.save(asignacion);
        }
    }

    @Transactional
    @Override
    public void toggleEquipoEstado(Integer idEquipo) {
        Equipos equipo = equiposRepository.findById(idEquipo)
                .orElseThrow(() -> new RuntimeException("Equipo no encontrado"));

        boolean nuevoActivo = equipo.getEstado() == EstadoActivoInactivo.I;
        equipo.setEstado(nuevoActivo ? EstadoActivoInactivo.A : EstadoActivoInactivo.I);

        equiposRepository.save(equipo);

        List<AsignacionUsuarioEquipo> asignaciones = asignacionUsuarioRepository.findAllByEquipo_IdEquipo(idEquipo);

        asignacionUbicacionDiaRepository.deleteAllByEquipo_IdEquipo(idEquipo);
        
        for (AsignacionUsuarioEquipo asignacion : asignaciones) {
            Usuario usuario = asignacion.getUsuario();
            Integer porcentajeAsignacion = asignacion.getPorcentajeTrabajo() == null ? 0
                    : asignacion.getPorcentajeTrabajo();
            Integer disponibilidadUsuario = usuario.getDisponibilidad() == null ? 0 : usuario.getDisponibilidad();

            if (!nuevoActivo) { // Deactivate team
                if (asignacion.getEstado() == EstadoActivoInactivo.A) {
                    // Только если раньше было А
                    usuario.setDisponibilidad(disponibilidadUsuario + porcentajeAsignacion);
                }
                asignacion.setEstado(EstadoActivoInactivo.I);
            } else { // Activate team
                if (disponibilidadUsuario >= porcentajeAsignacion) {
                    usuario.setDisponibilidad(disponibilidadUsuario - porcentajeAsignacion);
                    asignacion.setEstado(EstadoActivoInactivo.A);
                } else {
                    asignacion.setEstado(EstadoActivoInactivo.I);
                }
            }
        }

        // Guardar cambios
        asignacionUsuarioRepository.saveAll(asignaciones);

        usuarioisRepository.saveAll(asignaciones.stream().map(AsignacionUsuarioEquipo::getUsuario).toList());
    }
}
