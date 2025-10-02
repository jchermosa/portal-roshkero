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

    public void ajustarDisponibilidadConDelta(Usuario usuario, float delta) {
        if (delta > 0) {
            // Увеличиваем нагрузку → вычитаем из disponibilidad
            if (usuario.getDisponibilidad() == null || usuario.getDisponibilidad() < delta) {
                // !
                throw new ProcenteExisits(
                        "Usuario " + usuario.getIdUsuario() +
                                " no tiene suficiente disponibilidad. Actual: " + usuario.getDisponibilidad() +
                                ", requerido: " + delta);
            }
            usuario.setDisponibilidad(usuario.getDisponibilidad() - (int) delta);
        } else if (delta < 0) {
            // Уменьшение нагрузки → возвращаем обратно
            usuario.setDisponibilidad(usuario.getDisponibilidad() + (int) (-delta));
        }
        // if delta == 0 → save
        usuarioRepository.save(usuario);
    }

    public List<UsuarioAsignacionDto> updateUsers(Equipos equipo, List<UsuarioAsignacionDto> usuariosDto) {
        Map<Integer, AsignacionUsuarioEquipo> actualesMap = asignacionUsuarioRepository
                .findAllByEquipo_IdEquipo(equipo.getIdEquipo())
                .stream()
                .collect(Collectors.toMap(a -> a.getUsuario().getIdUsuario(), a -> a));

        List<UsuarioAsignacionDto> result = new ArrayList<>();

        if (usuariosDto != null) {
            for (UsuarioAsignacionDto uDto : usuariosDto) {
                Usuario usuario = usuarioRepository.findById(uDto.getIdUsuario())
                        .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + uDto.getIdUsuario()));

                // Считаем дельту относительно старого процента
                Integer viejoPorcentaje = Optional.ofNullable(
                        actualesMap.get(usuario.getIdUsuario()))
                        .map(AsignacionUsuarioEquipo::getPorcentajeTrabajo)
                        .orElse(0);

                Integer delta = uDto.getPorcentajeTrabajo() - viejoPorcentaje;

                // Проверка и применение
                ajustarDisponibilidadConDelta(usuario, delta);

                // Обновляем или создаём запись
                AsignacionUsuarioEquipo asignacion = actualesMap.get(usuario.getIdUsuario());
                if (asignacion != null) {
                    asignacion.setPorcentajeTrabajo(uDto.getPorcentajeTrabajo());
                    asignacion.setFechaEntrada(uDto.getFechaEntrada());
                    asignacion.setFechaFin(uDto.getFechaFin());
                } else {
                    asignacion = new AsignacionUsuarioEquipo();
                    asignacion.setEquipo(equipo);
                    asignacion.setUsuario(usuario);
                    asignacion.setPorcentajeTrabajo(uDto.getPorcentajeTrabajo());
                    asignacion.setFechaEntrada(uDto.getFechaEntrada());
                    asignacion.setFechaFin(uDto.getFechaFin());
                    asignacion.setFechaCreacion(LocalDateTime.now());
                }
                asignacionUsuarioRepository.save(asignacion);

                result.add(uDto);
            }

            // Удаляем пользователей, которых больше нет в списке
            for (AsignacionUsuarioEquipo asignacion : new ArrayList<>(actualesMap.values())) {
                if (usuariosDto.stream()
                        .noneMatch(u -> u.getIdUsuario().equals(asignacion.getUsuario().getIdUsuario()))) {
                    Usuario usuario = asignacion.getUsuario();
                    // Возвращаем процент доступности
                    ajustarDisponibilidadConDelta(usuario, -asignacion.getPorcentajeTrabajo());
                    asignacionUsuarioRepository.delete(asignacion);
                }
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