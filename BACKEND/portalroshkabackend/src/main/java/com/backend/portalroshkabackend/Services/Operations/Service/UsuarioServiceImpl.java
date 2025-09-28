package com.backend.portalroshkabackend.Services.Operations.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.portalroshkabackend.DTO.Operationes.UsuarioAsignacionDto;
import com.backend.portalroshkabackend.Models.AsignacionUsuarioEquipo;
import com.backend.portalroshkabackend.Models.Equipos;
import com.backend.portalroshkabackend.Models.Usuario;
import com.backend.portalroshkabackend.Repositories.OP.AsignacionUsuarioRepository;
import com.backend.portalroshkabackend.Repositories.OP.UsuarioisRepository;
import com.backend.portalroshkabackend.Services.Operations.Interface.IUsuarioService;

@Service
public class UsuarioServiceImpl implements IUsuarioService {

    @Autowired
    private UsuarioisRepository usuarioRepository;
    @Autowired
    private AsignacionUsuarioRepository asignacionUsuarioRepository;

    public Usuario getUsuarioById(Integer id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario not found: " + id));
    }

    public void ajustarDisponibilidadConDelta(Usuario usuario, float delta) {
        if (delta > 0) {
            // Увеличиваем нагрузку → вычитаем из disponibilidad
            if (usuario.getDisponibilidad() == null || usuario.getDisponibilidad() < delta) {
                throw new RuntimeException(
                        "Usuario " + usuario.getIdUsuario() +
                                " no tiene suficiente disponibilidad. Actual: " + usuario.getDisponibilidad() +
                                ", requerido: " + delta);
            }
            usuario.setDisponibilidad(usuario.getDisponibilidad() - (int) delta);
        } else if (delta < 0) {
            // Уменьшение нагрузки → возвращаем обратно
            usuario.setDisponibilidad(usuario.getDisponibilidad() + (int) (-delta));
        }
        // delta == 0 → ничего не делаем
        usuarioRepository.save(usuario);
    }

    public void devolverDisponibilidad(Usuario usuario, float porcentaje) {
        usuario.setDisponibilidad(usuario.getDisponibilidad() + (int) porcentaje);
        usuarioRepository.save(usuario);
    }

    public List<UsuarioAsignacionDto> updateUsers(Equipos equipo, List<UsuarioAsignacionDto> usuariosDto,
            Map<Integer, Usuario> usuariosValidados) {
        Map<Integer, AsignacionUsuarioEquipo> actualesMap = asignacionUsuarioRepository
                .findAllByEquipo_IdEquipo(equipo.getIdEquipo())
                .stream()
                .collect(Collectors.toMap(a -> a.getUsuario().getIdUsuario(), a -> a));

        List<UsuarioAsignacionDto> result = new ArrayList<>();

        if (usuariosDto != null) {
            for (UsuarioAsignacionDto uDto : usuariosDto) {
                Usuario usuario = usuariosValidados.get(uDto.getIdUsuario());

                float viejoPorcentaje = actualesMap.getOrDefault(usuario.getIdUsuario(), new AsignacionUsuarioEquipo())
                        .getPorcentajeTrabajo();
                float delta = uDto.getPorcentajeTrabajo() - viejoPorcentaje;
                ajustarDisponibilidadConDelta(usuario, delta);

                AsignacionUsuarioEquipo asignacion = actualesMap.get(usuario.getIdUsuario());
                if (asignacion != null) {
                    asignacion.setPorcentajeTrabajo(uDto.getPorcentajeTrabajo());
                    asignacion.setFechaEntrada(uDto.getFechaEntrada());
                    asignacion.setFechaFin(uDto.getFechaFin());
                    asignacionUsuarioRepository.save(asignacion);
                } else {
                    AsignacionUsuarioEquipo nueva = new AsignacionUsuarioEquipo();
                    nueva.setEquipo(equipo);
                    nueva.setUsuario(usuario);
                    nueva.setPorcentajeTrabajo(uDto.getPorcentajeTrabajo());
                    nueva.setFechaEntrada(uDto.getFechaEntrada());
                    nueva.setFechaFin(uDto.getFechaFin());
                    nueva.setFechaCreacion(LocalDateTime.now());
                    asignacionUsuarioRepository.save(nueva);
                }
                result.add(uDto);
            }

            // удалить пользователей, которых больше нет в команде
            for (AsignacionUsuarioEquipo asignacion : actualesMap.values()) {
                if (usuariosDto.stream()
                        .noneMatch(u -> u.getIdUsuario().equals(asignacion.getUsuario().getIdUsuario()))) {
                    Usuario usuario = asignacion.getUsuario();
                    usuario.setDisponibilidad(usuario.getDisponibilidad() + asignacion.getPorcentajeTrabajo());
                    usuarioRepository.save(usuario);
                    asignacionUsuarioRepository.delete(asignacion);
                }
            }
        }

        return result;
    }

    public List<UsuarioAsignacionDto> assignUsers(Equipos equipo, List<UsuarioAsignacionDto> usuariosDto,
            Map<Integer, Usuario> usuariosValidados) {
        List<UsuarioAsignacionDto> result = new ArrayList<>();
        if (usuariosDto != null) {
            for (UsuarioAsignacionDto uDto : usuariosDto) {
                Usuario usuario = usuariosValidados.get(uDto.getIdUsuario());
                usuario.setDisponibilidad(usuario.getDisponibilidad() - uDto.getPorcentajeTrabajo().intValue());
                usuarioRepository.save(usuario);

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