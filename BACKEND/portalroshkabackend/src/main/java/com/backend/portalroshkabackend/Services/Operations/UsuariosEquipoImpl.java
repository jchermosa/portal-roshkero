package com.backend.portalroshkabackend.Services.Operations;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.backend.portalroshkabackend.DTO.Operationes.UsuarioisResponseDto;
import com.backend.portalroshkabackend.DTO.Operationes.UsuariosEquipoCombinedResponseDto;
import com.backend.portalroshkabackend.DTO.Operationes.UsuariosEquipoResponseDto;
import com.backend.portalroshkabackend.Models.AsignacionUsuario;
import com.backend.portalroshkabackend.Models.Usuario;
import com.backend.portalroshkabackend.Repositories.AsignacionUsuarioRepository;
import com.backend.portalroshkabackend.Repositories.UserRepository;

@Service
public class UsuariosEquipoImpl implements IUsuarioisEquipoService {

    private final AsignacionUsuarioRepository asignacionUsuarioRepository;
    private final UserRepository userRepository;

    @Autowired
    public UsuariosEquipoImpl(AsignacionUsuarioRepository asignacionUsuarioRepository,
            UserRepository userRepository) {
        this.asignacionUsuarioRepository = asignacionUsuarioRepository;
        this.userRepository = userRepository;
    }

    // 1. Метод из интерфейса (для пагинации только пользователей в команде)
    @Override
    public UsuariosEquipoCombinedResponseDto getUsuariosEnYFueraDeEquipo(Integer idEquipo, Pageable pageable) {

        // 1️⃣ Пагинированные пользователи в команде
        Page<UsuariosEquipoResponseDto> usuariosEnEquipo = asignacionUsuarioRepository
                .findByEquiposIdEquipo(idEquipo, pageable)
                .map(asignacion -> {
                    UsuariosEquipoResponseDto dto = new UsuariosEquipoResponseDto();
                    dto.setIdAsignacionUsuarioEquipo(asignacion.getIdAsignacionUsuarioEquipo());
                    dto.setIdUsuario(asignacion.getIdUsuario());
                    dto.setIdTecnologia(asignacion.getIdTecnologia());
                    dto.setEquipos(asignacion.getEquipos());
                    dto.setFechaEntrada(asignacion.getFechaEntrada());
                    dto.setFechaFin(asignacion.getFechaFin());
                    dto.setPorcentajeTrabajo(asignacion.getPorcentajeTrabajo());
                    dto.setFechaCreacion(asignacion.getFechaCreacion());
                    return dto;
                });

        // 2️⃣ Пользователи вне команды (без пагинации)
        List<Integer> idsEnEquipo = usuariosEnEquipo.getContent()
                .stream()
                .map(u -> u.getIdUsuario().getIdUsuario())
                .toList();

        List<Usuario> usuariosNoEnEquipo = userRepository.findByIdUsuarioNotIn(idsEnEquipo);

        List<UsuarioisResponseDto> usuariosFueraEquipo = usuariosNoEnEquipo.stream()
                .map(u -> {
                    UsuarioisResponseDto dto = new UsuarioisResponseDto();
                    dto.setIdUsuario(u.getIdUsuario());
                    dto.setNombre(u.getNombre());
                    dto.setApellido(u.getApellido());
                    dto.setCorreo(u.getCorreo());
                    return dto;
                })
                .toList();

        // 3️⃣ Формируем комбинированный ответ
        UsuariosEquipoCombinedResponseDto response = new UsuariosEquipoCombinedResponseDto();
        response.setUsuariosEnEquipo(usuariosEnEquipo); // Page с пагинацией
        response.setUsuariosFueraEquipo(usuariosFueraEquipo); // List без пагинации

        return response;
    }
}
