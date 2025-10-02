package com.backend.portalroshkabackend.Services.Operations.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
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
        Set<Integer> incomingUserIds = usuariosDto != null
                ? usuariosDto.stream().map(UsuarioAsignacionDto::getIdUsuario).collect(Collectors.toSet())
                : Collections.emptySet();
        List<UsuarioAsignacionDto> result = new ArrayList<>();
        for (AsignacionUsuarioEquipo asignacion : actualesMap.values()) {
            if (!incomingUserIds.contains(asignacion.getUsuario().getIdUsuario())) {
                if (asignacion.getEstado() == EstadoActivoInactivo.A) {
                    // вернуть проценты только если был активен
                    Usuario usuario = asignacion.getUsuario();
                    usuario.setDisponibilidad(usuario.getDisponibilidad() + asignacion.getPorcentajeTrabajo());
                    usuarioRepository.save(usuario);
                }
                asignacionUsuarioRepository.delete(asignacion);
            }
        }
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

            // Если пользователь активный, проверяем и вычитаем проценты
            if (uDto.getEstado() == EstadoActivoInactivo.A) {

                if (uDto.getPorcentajeTrabajo() == null || uDto.getPorcentajeTrabajo() <= 0) {
                    throw new IllegalArgumentException(
                            "Porcentaje de trabajo inválido para usuario " + uDto.getIdUsuario());
                }

                int nuevaDisponibilidad = usuario.getDisponibilidad() - uDto.getPorcentajeTrabajo();
                if (nuevaDisponibilidad < 0) {
                    throw new ProcenteExisits("Usuario " + usuario.getIdUsuario() +
                            " no tiene suficiente disponibilidad. Actual: " + usuario.getDisponibilidad() +
                            ", requerido: " + uDto.getPorcentajeTrabajo());
                }

                usuario.setDisponibilidad(nuevaDisponibilidad);
                usuarioRepository.save(usuario);
            }

            // Если пользователь inactivo – ничего не делаем с disponibilidad
            result.add(uDto);
        }
        return result;
    }

    public List<UsuarioAsignacionDto> assignUsers(Integer equipoId, List<UsuarioAsignacionDto> usuariosDto) {
        List<UsuarioAsignacionDto> result = new ArrayList<>();
        if (usuariosDto != null) {
            for (UsuarioAsignacionDto uDto : usuariosDto) {
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
                asignacion.setEstado(uDto.getEstado());

                asignacionUsuarioRepository.save(asignacion);
                result.add(uDto);
            }
        }
        return result;
    }
}