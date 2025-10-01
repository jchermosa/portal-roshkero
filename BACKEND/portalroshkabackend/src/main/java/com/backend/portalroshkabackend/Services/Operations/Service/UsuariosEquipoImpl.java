package com.backend.portalroshkabackend.Services.Operations.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.backend.portalroshkabackend.DTO.Operationes.UsuarioEquipoRequestDto;
import com.backend.portalroshkabackend.DTO.Operationes.UsuarioisResponseDto;
import com.backend.portalroshkabackend.DTO.Operationes.UsuariosEquipoCombinedResponseDto;
import com.backend.portalroshkabackend.DTO.Operationes.Metadatas.UsuariosEquipoResponseDto;
import com.backend.portalroshkabackend.Models.AsignacionUsuarioEquipo;
import com.backend.portalroshkabackend.Models.Equipos;
import com.backend.portalroshkabackend.Models.Tecnologias;
import com.backend.portalroshkabackend.Models.Usuario;
import com.backend.portalroshkabackend.Repositories.OP.AsignacionUsuarioRepository;
import com.backend.portalroshkabackend.Repositories.OP.EquiposRepository;
import com.backend.portalroshkabackend.Repositories.OP.TecnologiaRepository;
import com.backend.portalroshkabackend.Repositories.TH.UserRepository;
import com.backend.portalroshkabackend.Services.Operations.Interface.IUsuarioisEquipoService;

@Service
public class UsuariosEquipoImpl implements IUsuarioisEquipoService {

        private final AsignacionUsuarioRepository asignacionUsuarioRepository;
        private final UserRepository userRepository;
        private final TecnologiaRepository tecnologiaRepository;
        private final EquiposRepository equiposRepository;

        @Autowired
        public UsuariosEquipoImpl(AsignacionUsuarioRepository asignacionUsuarioRepository,
                        UserRepository userRepository,
                        TecnologiaRepository tecnologiaRepository,
                        EquiposRepository equiposRepository) {
                this.asignacionUsuarioRepository = asignacionUsuarioRepository;
                this.userRepository = userRepository;
                this.tecnologiaRepository = tecnologiaRepository;
                this.equiposRepository = equiposRepository;
        }

        // 1. Метод из интерфейса (для пагинации только пользователей в команде)
        @Override
        public UsuariosEquipoCombinedResponseDto getUsuariosEnYFueraDeEquipo(Integer idEquipo, Pageable pageable) {

                // 1️⃣ Пагинированные пользователи в команде
                Page<UsuariosEquipoResponseDto> usuariosEnEquipo = asignacionUsuarioRepository
                                .findByEquipo_IdEquipo(idEquipo, pageable)
                                .map(asignacion -> {
                                        UsuariosEquipoResponseDto dto = new UsuariosEquipoResponseDto();
                                        dto.setIdAsignacionUsuarioEquipo(asignacion.getIdAsignacionUsuarioEquipo());
                                        dto.setIdUsuario(asignacion.getUsuario());
                                        dto.setEquipos(asignacion.getEquipo());
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

                List<Tecnologias> tecnologias = tecnologiaRepository.findAll();

                // 3️⃣ Формируем комбинированный ответ
                UsuariosEquipoCombinedResponseDto response = new UsuariosEquipoCombinedResponseDto();
                response.setUsuariosEnEquipo(usuariosEnEquipo); // Page с пагинацией
                response.setUsuariosFueraEquipo(usuariosFueraEquipo); // List без пагинации
                response.setTecnologias(tecnologias);

                return response;
        }

        @Override
        public UsuariosEquipoResponseDto addUsuarioToEquipo(Integer idEquipo, UsuarioEquipoRequestDto requestDto) {

                // // Проверяем, существует ли уже пользователь в этой команде
                // boolean exists =
                // asignacionUsuarioRepository.existsByIdUsuarioAndEquiposIdEquipo(
                // requestDto.getIdUsuario(), idEquipo);

                // if (exists) {
                // throw new RuntimeException("El usuario ya está asignado a este equipo");
                // }

                // Проверка существования команды
                Equipos equipo = equiposRepository.findById(idEquipo)
                                .orElseThrow(() -> new RuntimeException("Equipo no encontrado"));

                // Проверка существования пользователя
                Usuario usuario = userRepository.findById(requestDto.getIdUsuario())
                                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

                // Проверка существования технологии
                Tecnologias tecnologia = tecnologiaRepository.findById(requestDto.getIdTecnologia())
                                .orElseThrow(() -> new RuntimeException("Tecnologia no encontrada"));

                // Создание новой ассоциации
                AsignacionUsuarioEquipo asignacion = new AsignacionUsuarioEquipo();
                Integer porcentajeAsignado = requestDto.getPorcentajeTrabajo();
                int disponibilidadActual = usuario.getDisponibilidad();

                if (porcentajeAsignado > disponibilidadActual) {
                        throw new RuntimeException("El usuario no tiene suficiente disponibilidad");
                }

                // Вычитаем проценты
                usuario.setDisponibilidad(disponibilidadActual - porcentajeAsignado.intValue());
                userRepository.save(usuario);

                // Создаём назначение
                asignacion.setEquipo(equipo);
                asignacion.setUsuario(usuario);
                asignacion.setFechaEntrada(requestDto.getFechaEntrada());
                asignacion.setFechaFin(requestDto.getFechaFin());
                asignacion.setPorcentajeTrabajo(porcentajeAsignado);
                asignacion.setFechaCreacion(LocalDateTime.now());

                asignacionUsuarioRepository.save(asignacion);

                // Преобразуем в DTO
                UsuariosEquipoResponseDto response = new UsuariosEquipoResponseDto();
                response.setIdAsignacionUsuarioEquipo(asignacion.getIdAsignacionUsuarioEquipo());
                response.setIdUsuario(usuario);
                response.setIdTecnologia(tecnologia);
                response.setEquipos(equipo);
                response.setFechaEntrada(asignacion.getFechaEntrada());
                response.setFechaFin(asignacion.getFechaFin());
                response.setPorcentajeTrabajo(asignacion.getPorcentajeTrabajo());
                response.setFechaCreacion(asignacion.getFechaCreacion());

                return response;
        }

}
