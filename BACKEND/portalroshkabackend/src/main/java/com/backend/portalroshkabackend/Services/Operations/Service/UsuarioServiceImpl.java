package com.backend.portalroshkabackend.Services.Operations.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.portalroshkabackend.DTO.Operationes.UsuarioAsignacionDto;
import com.backend.portalroshkabackend.Exception.ProcenteExisits;
import com.backend.portalroshkabackend.Models.AsignacionUsuarioEquipo;
import com.backend.portalroshkabackend.Models.Equipos;
import com.backend.portalroshkabackend.Models.Usuario;
import com.backend.portalroshkabackend.Models.Enum.EstadoActivoInactivo;
import com.backend.portalroshkabackend.Repositories.OP.AsignacionUsuarioRepository;
import com.backend.portalroshkabackend.Repositories.OP.EquiposRepository;
import com.backend.portalroshkabackend.Repositories.OP.UsuarioisRepository;
import com.backend.portalroshkabackend.Services.Operations.Interface.IUsuarioService;

@Service
public class UsuarioServiceImpl implements IUsuarioService {

    @Autowired
    private UsuarioisRepository usuarioRepository;
    @Autowired
    private AsignacionUsuarioRepository asignacionUsuarioRepository;
    @Autowired
    private EquiposRepository equiposRepository;

    public void ajustarDisponibilidadConDelta(Usuario usuario, Integer delta) {
        if (delta > 0) {
            // уменьшаем доступность
            if (usuario.getDisponibilidad() == null || usuario.getDisponibilidad() < delta) {
                throw new ProcenteExisits(
                        "Usuario " + usuario.getIdUsuario() +
                                " no tiene suficiente disponibilidad. Actual: " + usuario.getDisponibilidad() +
                                ", requerido: " + delta);
            }
            usuario.setDisponibilidad(usuario.getDisponibilidad() - delta);
        } else if (delta < 0) {
            // возвращаем (увеличиваем) доступность
            usuario.setDisponibilidad(usuario.getDisponibilidad() + Math.abs(delta));
        }
        // сохраняем пользователя (внутри этого метода — единственное место, меняющее
        // disponibilidad)
        usuarioRepository.save(usuario);
    }

    public List<UsuarioAsignacionDto> updateUsers(EstadoActivoInactivo estadoEquipo, Equipos equipo,
            List<UsuarioAsignacionDto> usuariosDto) {
        Map<Integer, AsignacionUsuarioEquipo> actualesMap = asignacionUsuarioRepository
                .findAllByEquipo_IdEquipo(equipo.getIdEquipo())
                .stream()
                .collect(Collectors.toMap(a -> a.getUsuario().getIdUsuario(), a -> a));

        List<UsuarioAsignacionDto> result = new ArrayList<>();

        if (usuariosDto != null) {
            for (UsuarioAsignacionDto uDto : usuariosDto) {
                Usuario usuario = usuarioRepository.findById(uDto.getIdUsuario())
                        .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + uDto.getIdUsuario()));

                AsignacionUsuarioEquipo asignacion = actualesMap.get(usuario.getIdUsuario());
                EstadoActivoInactivo estadoAnterior = asignacion != null ? asignacion.getEstado() : null;

                // --- если назначение уже есть ---
                if (asignacion != null) {
                    asignacion.setFechaEntrada(uDto.getFechaEntrada());
                    asignacion.setFechaFin(uDto.getFechaFin());

                    // меняем статус
                    if (estadoAnterior == EstadoActivoInactivo.A && uDto.getEstado() == EstadoActivoInactivo.I) {
                        // A → I : вернуть проценты
                        usuario.setDisponibilidad(usuario.getDisponibilidad() + asignacion.getPorcentajeTrabajo());
                    } else if (estadoAnterior == EstadoActivoInactivo.I && uDto.getEstado() == EstadoActivoInactivo.A) {
                        // I → A : списать новые проценты
                        ajustarDisponibilidadConDelta(usuario, uDto.getPorcentajeTrabajo());
                    } else if (estadoAnterior == EstadoActivoInactivo.A && uDto.getEstado() == EstadoActivoInactivo.A) {
                        // A → A : пересчитать дельту
                        Integer viejo = asignacion.getPorcentajeTrabajo();
                        Integer delta = uDto.getPorcentajeTrabajo() - viejo;
                        ajustarDisponibilidadConDelta(usuario, delta);
                    }

                    asignacion.setPorcentajeTrabajo(uDto.getPorcentajeTrabajo());
                    asignacion.setEstado(uDto.getEstado());

                } else {
                    asignacion = new AsignacionUsuarioEquipo();
                    asignacion.setEquipo(equipo);
                    asignacion.setUsuario(usuario);
                    asignacion.setFechaEntrada(uDto.getFechaEntrada());
                    asignacion.setFechaFin(uDto.getFechaFin());
                    asignacion.setFechaCreacion(LocalDateTime.now());
                    asignacion.setEstado(uDto.getEstado());
                    asignacion.setPorcentajeTrabajo(uDto.getPorcentajeTrabajo());

                    if (uDto.getEstado() == EstadoActivoInactivo.A) {
                        ajustarDisponibilidadConDelta(usuario, uDto.getPorcentajeTrabajo());
                    }
                }

                asignacionUsuarioRepository.save(asignacion);
                usuarioRepository.save(usuario);
                result.add(uDto);
            }
        }

        return result;
    }

    public List<UsuarioAsignacionDto> calculateprocenteusuarios(List<UsuarioAsignacionDto> usuariosDto) {
        if (usuariosDto == null || usuariosDto.isEmpty()) {
            return null;
        }

        List<UsuarioAsignacionDto> result = new ArrayList<>();
        for (UsuarioAsignacionDto uDto : usuariosDto) {
            Usuario usuario = usuarioRepository.findById(uDto.getIdUsuario())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + uDto.getIdUsuario()));

            if (uDto.getPorcentajeTrabajo() == null || uDto.getPorcentajeTrabajo() <= 0) {
                throw new IllegalArgumentException(
                        "Porcentaje de trabajo inválido para usuario " + uDto.getIdUsuario());
            }

            int nuevaDisponibilidad = usuario.getDisponibilidad() - uDto.getPorcentajeTrabajo().intValue();
            if (nuevaDisponibilidad < 0) {
                throw new ProcenteExisits("Usuario " + usuario.getIdUsuario() +
                        " no tiene suficiente disponibilidad. Actual: " + usuario.getDisponibilidad() +
                        ", requerido: " + uDto.getPorcentajeTrabajo().intValue());
            }

            usuario.setDisponibilidad(nuevaDisponibilidad);
            usuarioRepository.save(usuario);
            result.add(uDto);
        }
        return result;
    }

    public List<UsuarioAsignacionDto> assignUsers(Integer equipoId, List<UsuarioAsignacionDto> usuariosDto) {
        List<UsuarioAsignacionDto> result = new ArrayList<>();
        if (usuariosDto != null) {
            for (UsuarioAsignacionDto uDto : usuariosDto) {
                // Проверка и уменьшение доступности
                Equipos equipo = equiposRepository.findByIdEquipo(equipoId);
                Usuario usuario = usuarioRepository.findById(uDto.getIdUsuario())
                        .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + uDto.getIdUsuario()));
                AsignacionUsuarioEquipo asignacion = new AsignacionUsuarioEquipo();
                asignacion.setEquipo(equipo);
                asignacion.setUsuario(usuario);
                asignacion.setPorcentajeTrabajo(uDto.getPorcentajeTrabajo());
                asignacion.setFechaEntrada(uDto.getFechaEntrada());
                asignacion.setFechaFin(uDto.getFechaFin());
                asignacion.setFechaCreacion(LocalDateTime.now());
                asignacionUsuarioRepository.save(asignacion);

                result.add(uDto);
            }
        }
        return result;
    }
}