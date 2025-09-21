package com.backend.portalroshkabackend.Services.Operations.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

import com.backend.portalroshkabackend.DTO.Operationes.EquiposRequestDto;
import com.backend.portalroshkabackend.DTO.Operationes.EquiposResponseDto;
import com.backend.portalroshkabackend.DTO.Operationes.UsuarioAsignacionDto;
import com.backend.portalroshkabackend.DTO.Operationes.UsuarioisResponseDto;
import com.backend.portalroshkabackend.Models.AsignacionUsuario;
import com.backend.portalroshkabackend.Models.Clientes;
import com.backend.portalroshkabackend.Models.Equipos;
import com.backend.portalroshkabackend.Models.Tecnologias;
import com.backend.portalroshkabackend.Models.TecnologiasEquipos;
import com.backend.portalroshkabackend.Models.Usuario;
import com.backend.portalroshkabackend.Models.Enum.EstadoActivoInactivo;
import com.backend.portalroshkabackend.Repositories.EquiposRepository;
import com.backend.portalroshkabackend.Repositories.TecnologiaRepository;
import com.backend.portalroshkabackend.Repositories.TecnologiasEquiposRepository;
import com.backend.portalroshkabackend.Repositories.UsuarioisRepository;
import com.backend.portalroshkabackend.Services.Operations.Interface.IEquiposService;
import com.backend.portalroshkabackend.Services.Operations.Interface.ITecnologiaService;
import com.backend.portalroshkabackend.Services.Operations.Interface.IUsuarioService;
import com.backend.portalroshkabackend.Repositories.AsignacionUsuarioRepository;
import com.backend.portalroshkabackend.Repositories.ClientesRepository;

@Service("operationsEquiposService")
@Validated
public class EquiposServiceImpl implements IEquiposService {

    private final EquiposRepository equiposRepository;
    private final ClientesRepository clientesRepository;
    private final TecnologiaRepository tecnologiasRepository;
    private final TecnologiasEquiposRepository tecnologiasEquiposRepository;
    private final UsuarioisRepository usuarioisRepository;
    private final AsignacionUsuarioRepository asignacionUsuarioRepository;
    private final IUsuarioService usuarioService;
    private final ITecnologiaService tecnologiaService;

    private final Map<String, Function<Pageable, Page<Equipos>>> sortingMap;

    @Autowired
    public EquiposServiceImpl(EquiposRepository equiposRepository, ClientesRepository clientesRepository,
            TecnologiaRepository tecnologiasRepository, TecnologiasEquiposRepository tecnologiasEquiposRepository,
            UsuarioisRepository usuarioisRepository, AsignacionUsuarioRepository asignacionUsuarioRepository,
            IUsuarioService usuarioService,
            ITecnologiaService tecnologiaService) {
        this.equiposRepository = equiposRepository;
        this.clientesRepository = clientesRepository;
        this.tecnologiasRepository = tecnologiasRepository;
        this.tecnologiasEquiposRepository = tecnologiasEquiposRepository;
        this.usuarioisRepository = usuarioisRepository;
        this.asignacionUsuarioRepository = asignacionUsuarioRepository;
        this.usuarioService = usuarioService;
        this.tecnologiaService = tecnologiaService;

        sortingMap = new HashMap<>();

        // nombre
        sortingMap.put("nombreAsc", equiposRepository::findAllByOrderByNombreAsc);
        sortingMap.put("nombreDesc", equiposRepository::findAllByOrderByNombreDesc);

        // cliente
        sortingMap.put("clienteAsc", equiposRepository::findAllByOrderByCliente_NombreAsc);
        sortingMap.put("clienteDesc", equiposRepository::findAllByOrderByCliente_NombreDesc);

        // lider
        sortingMap.put("liderAsc", equiposRepository::findAllByOrderByLider_NombreAsc);
        sortingMap.put("liderDesc", equiposRepository::findAllByOrderByLider_NombreDesc);

        // estado
        sortingMap.put("estadoAsc", equiposRepository::findAllByOrderByEstadoAsc);
        sortingMap.put("estadoDesc", equiposRepository::findAllByOrderByEstadoDesc);

        sortingMap.put("default", equiposRepository::findAll);
    }

    @Override
    public EquiposResponseDto getTeamById(Integer id) {
        Equipos e = equiposRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Equipo not found"));

        EquiposResponseDto dto = new EquiposResponseDto();
        dto.setIdEquipo(e.getIdEquipo());

        if (e.getLider() != null) {
            Usuario u = e.getLider();
            dto.setLider(new UsuarioisResponseDto(
                    u.getIdUsuario(),
                    u.getNombre(),
                    u.getApellido(),
                    u.getCorreo(),
                    u.getDisponibilidad()));
        }

        dto.setNombre(e.getNombre());
        dto.setFechaInicio(e.getFechaInicio());
        dto.setFechaLimite(e.getFechaLimite());
        dto.setCliente(e.getCliente());
        dto.setFechaCreacion(e.getFechaCreacion());
        dto.setEstado(e.getEstado());

        List<TecnologiasEquipos> tecs = tecnologiasEquiposRepository.findAllByEquipo_IdEquipo(e.getIdEquipo());
        List<Tecnologias> tecnologias = tecs.stream()
                .map(TecnologiasEquipos::getTecnologia)
                .toList();
        dto.setTecnologias(tecnologias);

        // --- Пользователи с porcentaje_trabajo ---
        List<AsignacionUsuario> asignaciones = asignacionUsuarioRepository.findAllByEquipo_IdEquipo(id);
        List<UsuarioAsignacionDto> usuariosAsignacion = asignaciones.stream()
                .map(asig -> new UsuarioAsignacionDto(
                        asig.getUsuario().getIdUsuario(),
                        asig.getUsuario().getNombre(),
                        asig.getUsuario().getApellido(),
                        asig.getUsuario().getCorreo(),
                        asig.getPorcentajeTrabajo(),
                        asig.getFechaEntrada(),
                        asig.getFechaFin()))
                .toList();

        dto.setUsuariosAsignacion(usuariosAsignacion);

        return dto;
    }

    // Метод для получения команд с сортировкой
    @Override
    public Page<EquiposResponseDto> getTeamsSorted(Pageable pageable, String sortBy) {
        Function<Pageable, Page<Equipos>> func = sortingMap.getOrDefault(sortBy, sortingMap.get("default"));
        Page<Equipos> page = func.apply(pageable);
        return page.map(this::mapToDto);
    }

    // Метод маппинга сущности в DTO
    public EquiposResponseDto mapToDto(Equipos e) {
        EquiposResponseDto dto = new EquiposResponseDto();
        dto.setIdEquipo(e.getIdEquipo());
        if (e.getLider() != null) {
            Usuario u = e.getLider();
            dto.setLider(new UsuarioisResponseDto(
                    u.getIdUsuario(),
                    u.getNombre(),
                    u.getApellido(),
                    u.getCorreo(),
                    u.getDisponibilidad()));
        }
        dto.setNombre(e.getNombre());
        dto.setFechaInicio(e.getFechaInicio());
        dto.setFechaLimite(e.getFechaLimite());
        dto.setCliente(e.getCliente());
        dto.setFechaCreacion(e.getFechaCreacion());
        dto.setEstado(e.getEstado());

        List<TecnologiasEquipos> tecs = tecnologiasEquiposRepository.findAllByEquipo_IdEquipo(e.getIdEquipo());
        List<Tecnologias> tecnologias = tecs.stream()
                .map(TecnologiasEquipos::getTecnologia)
                .toList();

        dto.setTecnologias(tecnologias);
        return dto;
    }

    @Override
    public EquiposResponseDto postNewTeam(EquiposRequestDto requestDto) {

        // --- Проверка имени команды ---
        List<Equipos> existentes = equiposRepository.findAllByNombre(requestDto.getNombre().trim());
        if (!existentes.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "nombre: El nombre ya existe");
        }

        // --- Проверка лидера ---
        Usuario lider = usuarioisRepository.findById(requestDto.getIdLider())
                .orElseThrow(() -> new RuntimeException("Lider not found"));

        // --- Проверка клиента ---
        Clientes cliente = clientesRepository.findById(requestDto.getIdCliente())
                .orElseThrow(() -> new RuntimeException("Cliente not found"));

        // --- Проверка пользователей ---
        if (requestDto.getUsuarios() != null) {
            for (UsuarioAsignacionDto uDto : requestDto.getUsuarios()) {
                Usuario usuario = usuarioisRepository.findById(uDto.getIdUsuario())
                        .orElseThrow(() -> new RuntimeException("Usuario not found: " + uDto.getIdUsuario()));

                if (uDto.getPorcentajeTrabajo() == null) {
                    throw new RuntimeException("PorcentajeTrabajo obligatorio para usuario " + usuario.getIdUsuario());
                }

                if (usuario.getDisponibilidad() == null || usuario.getDisponibilidad() < uDto.getPorcentajeTrabajo()) {
                    throw new RuntimeException(
                            "Usuario " + usuario.getIdUsuario() + " no tiene suficiente disponibilidad");
                }
            }
        }

        // --- Создание команды ---
        Equipos equipo = new Equipos();
        equipo.setNombre(requestDto.getNombre());
        equipo.setLider(lider);
        equipo.setFechaInicio(requestDto.getFechaInicio());
        equipo.setFechaLimite(requestDto.getFechaLimite());
        equipo.setCliente(cliente);
        equipo.setEstado(EstadoActivoInactivo.valueOf(requestDto.getEstado()));
        equipo.setFechaCreacion(new Date(System.currentTimeMillis()));

        Equipos savedEquipo = equiposRepository.save(equipo);

        // --- DTO лидера ---
        UsuarioisResponseDto liderDto = new UsuarioisResponseDto(
                lider.getIdUsuario(),
                lider.getNombre(),
                lider.getApellido(),
                lider.getCorreo(),
                lider.getDisponibilidad());

        // --- Технологии ---
        List<Tecnologias> tecnologias = new ArrayList<>();
        if (requestDto.getIdTecnologias() != null) {
            for (Integer idTec : requestDto.getIdTecnologias()) {
                Tecnologias tecnologia = tecnologiasRepository.findByIdTecnologia(idTec);

                TecnologiasEquipos tecEquipo = new TecnologiasEquipos();
                tecEquipo.setEquipo(savedEquipo);
                tecEquipo.setTecnologia(tecnologia);
                tecnologiasEquiposRepository.save(tecEquipo);

                tecnologias.add(tecnologia);
            }
        }

        // --- Привязка пользователей к команде ---
        List<UsuarioAsignacionDto> usuariosDto = new ArrayList<>();
        if (requestDto.getUsuarios() != null) {
            for (UsuarioAsignacionDto uDto : requestDto.getUsuarios()) {
                Usuario usuario = usuarioisRepository.findById(uDto.getIdUsuario()).get();

                // Уменьшаем disponibilidad у пользователя
                usuario.setDisponibilidad(usuario.getDisponibilidad() - uDto.getPorcentajeTrabajo().intValue());
                usuarioisRepository.save(usuario);

                // Создаем запись в asignacion_usuario_equipo
                AsignacionUsuario asignacion = new AsignacionUsuario();
                asignacion.setEquipo(savedEquipo);
                asignacion.setUsuario(usuario);
                asignacion.setFechaEntrada(uDto.getFechaEntrada());
                asignacion.setFechaFin(uDto.getFechaFin());
                asignacion.setPorcentajeTrabajo(uDto.getPorcentajeTrabajo());
                asignacion.setFechaCreacion(new Date(System.currentTimeMillis()));
                asignacionUsuarioRepository.save(asignacion);

                // DTO для ответа
                usuariosDto.add(new UsuarioAsignacionDto(
                        usuario.getIdUsuario(),
                        usuario.getNombre(),
                        usuario.getApellido(),
                        usuario.getCorreo(),
                        uDto.getPorcentajeTrabajo(),
                        uDto.getFechaEntrada(),
                        uDto.getFechaFin()));
            }
        }

        // --- Формирование ответа ---
        EquiposResponseDto responseDto = new EquiposResponseDto();
        responseDto.setIdEquipo(savedEquipo.getIdEquipo());
        responseDto.setNombre(savedEquipo.getNombre());
        responseDto.setCliente(savedEquipo.getCliente());
        responseDto.setLider(liderDto);
        responseDto.setFechaInicio(savedEquipo.getFechaInicio());
        responseDto.setFechaLimite(savedEquipo.getFechaLimite());
        responseDto.setEstado(savedEquipo.getEstado());
        responseDto.setTecnologias(tecnologias);
        responseDto.setUsuariosAsignacion(usuariosDto);
        responseDto.setFechaCreacion(savedEquipo.getFechaCreacion());

        return responseDto;
    }

    @Override
    public EquiposResponseDto updateTeam(Integer id, EquiposRequestDto requestDto) {
        Equipos equipo = equiposRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Equipo not found"));

        // 1️⃣ Обновляем основные поля
        if (requestDto.getNombre() != null && !requestDto.getNombre().trim().isEmpty()
                && !equipo.getNombre().equals(requestDto.getNombre().trim())) {
            if (!equiposRepository.findAllByNombre(requestDto.getNombre().trim()).isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "nombre: El nombre ya existe");
            }
            equipo.setNombre(requestDto.getNombre().trim());
        }

        if (requestDto.getIdLider() != null
                && !equipo.getLider().getIdUsuario().equals(requestDto.getIdLider())) {
            Usuario nuevoLider = usuarioService.getUsuarioById(requestDto.getIdLider());
            equipo.setLider(nuevoLider);
        }

        if (requestDto.getIdCliente() != null
                && !equipo.getCliente().getIdCliente().equals(requestDto.getIdCliente())) {
            Clientes nuevoCliente = clientesRepository.findById(requestDto.getIdCliente())
                    .orElseThrow(() -> new RuntimeException("Cliente not found"));
            equipo.setCliente(nuevoCliente);
        }

        if (requestDto.getFechaInicio() != null)
            equipo.setFechaInicio(requestDto.getFechaInicio());
        if (requestDto.getFechaLimite() != null)
            equipo.setFechaLimite(requestDto.getFechaLimite());
        if (requestDto.getEstado() != null)
            equipo.setEstado(EstadoActivoInactivo.valueOf(requestDto.getEstado()));

        // 2️⃣ Обновляем технологии
        if (requestDto.getIdTecnologias() != null) {
            tecnologiaService.updateTecnologiasEquipo(equipo, requestDto.getIdTecnologias());
        }

        // 3️⃣ Сохраняем команду
        Equipos savedEquipo = equiposRepository.save(equipo);

        // 4️⃣ Обновляем пользователей и их asignaciones
        Map<Integer, AsignacionUsuario> actualesMap = asignacionUsuarioRepository
                .findAllByEquipo_IdEquipo(id)
                .stream()
                .collect(Collectors.toMap(a -> a.getUsuario().getIdUsuario(), a -> a));

        List<UsuarioAsignacionDto> usuariosDto = new ArrayList<>();

        if (requestDto.getUsuarios() != null) {
            for (UsuarioAsignacionDto uDto : requestDto.getUsuarios()) {
                Usuario usuario = usuarioService.getUsuarioById(uDto.getIdUsuario());

                float viejoPorcentaje = actualesMap.containsKey(usuario.getIdUsuario())
                        ? actualesMap.get(usuario.getIdUsuario()).getPorcentajeTrabajo()
                        : 0f;

                float delta = uDto.getPorcentajeTrabajo() - viejoPorcentaje;

                // Корректно обновляем disponibilidad
                usuarioService.ajustarDisponibilidadConDelta(usuario, delta);

                if (actualesMap.containsKey(usuario.getIdUsuario())) {
                    AsignacionUsuario asignacionExistente = actualesMap.get(usuario.getIdUsuario());
                    asignacionExistente.setPorcentajeTrabajo(uDto.getPorcentajeTrabajo());
                    asignacionExistente.setFechaEntrada(uDto.getFechaEntrada());
                    asignacionExistente.setFechaFin(uDto.getFechaFin());
                    asignacionUsuarioRepository.save(asignacionExistente);
                } else {
                    AsignacionUsuario nuevaAsignacion = new AsignacionUsuario();
                    nuevaAsignacion.setEquipo(savedEquipo);
                    nuevaAsignacion.setUsuario(usuario);
                    nuevaAsignacion.setPorcentajeTrabajo(uDto.getPorcentajeTrabajo());
                    nuevaAsignacion.setFechaEntrada(uDto.getFechaEntrada());
                    nuevaAsignacion.setFechaFin(uDto.getFechaFin());
                    nuevaAsignacion.setFechaCreacion(new Date(System.currentTimeMillis()));
                    asignacionUsuarioRepository.save(nuevaAsignacion);
                }

                usuariosDto.add(uDto);
            }

            // Удаляем пользователей, которых нет в запросе
            for (AsignacionUsuario asignacion : actualesMap.values()) {
                if (requestDto.getUsuarios().stream()
                        .noneMatch(u -> u.getIdUsuario().equals(asignacion.getUsuario().getIdUsuario()))) {
                    Usuario usuario = asignacion.getUsuario();
                    usuario.setDisponibilidad(
                            usuario.getDisponibilidad() + asignacion.getPorcentajeTrabajo().intValue());
                    usuarioisRepository.save(usuario);
                    asignacionUsuarioRepository.delete(asignacion);
                }
            }
        }

        // 5️⃣ Формируем DTO ответа
        UsuarioisResponseDto liderDto = new UsuarioisResponseDto(
                savedEquipo.getLider().getIdUsuario(),
                savedEquipo.getLider().getNombre(),
                savedEquipo.getLider().getApellido(),
                savedEquipo.getLider().getCorreo(),
                savedEquipo.getLider().getDisponibilidad());

        List<Tecnologias> tecnologias = tecnologiasEquiposRepository.findAllByEquipo_IdEquipo(savedEquipo.getIdEquipo())
                .stream().map(TecnologiasEquipos::getTecnologia).toList();

        EquiposResponseDto responseDto = new EquiposResponseDto();
        responseDto.setIdEquipo(savedEquipo.getIdEquipo());
        responseDto.setNombre(savedEquipo.getNombre());
        responseDto.setCliente(savedEquipo.getCliente());
        responseDto.setLider(liderDto);
        responseDto.setFechaInicio(savedEquipo.getFechaInicio());
        responseDto.setFechaLimite(savedEquipo.getFechaLimite());
        responseDto.setEstado(savedEquipo.getEstado());
        responseDto.setTecnologias(tecnologias);
        responseDto.setUsuariosAsignacion(usuariosDto);
        responseDto.setFechaCreacion(savedEquipo.getFechaCreacion());

        return responseDto;
    }

    @Override
    public void deleteTeam(int id_equipo) {
        Equipos equipo = equiposRepository.findById(id_equipo)
                .orElseThrow(() -> new RuntimeException("Team not found"));
        equipo.setEstado(EstadoActivoInactivo.I); //
        equiposRepository.save(equipo);
    }

}
