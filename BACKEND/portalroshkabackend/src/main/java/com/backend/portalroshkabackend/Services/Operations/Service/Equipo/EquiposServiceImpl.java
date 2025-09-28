package com.backend.portalroshkabackend.Services.Operations.Service.Equipo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.server.ResponseStatusException;

import com.backend.portalroshkabackend.DTO.Operationes.EquipoDiaUbicacionResponceDto;
import com.backend.portalroshkabackend.DTO.Operationes.EquiposRequestDto;
import com.backend.portalroshkabackend.DTO.Operationes.EquiposResponseDto;
import com.backend.portalroshkabackend.DTO.Operationes.TecnologiasDto;
import com.backend.portalroshkabackend.DTO.Operationes.UsuarioAsignacionDto;
import com.backend.portalroshkabackend.DTO.Operationes.UsuarioisResponseDto;
import com.backend.portalroshkabackend.DTO.Operationes.Metadatas.ClientesResponseDto;
import com.backend.portalroshkabackend.Exception.DisponibilidadInsuficienteException;
import com.backend.portalroshkabackend.Exception.NombreDuplicadoException;
import com.backend.portalroshkabackend.Models.AsignacionUsuarioEquipo;
import com.backend.portalroshkabackend.Models.Clientes;
import com.backend.portalroshkabackend.Models.Equipos;
import com.backend.portalroshkabackend.Models.Tecnologias;
import com.backend.portalroshkabackend.Models.TecnologiasEquipos;
import com.backend.portalroshkabackend.Models.Usuario;
import com.backend.portalroshkabackend.Models.Enum.EstadoActivoInactivo;
import com.backend.portalroshkabackend.Repositories.OP.AsignacionUsuarioRepository;
import com.backend.portalroshkabackend.Repositories.OP.ClientesRepository;
import com.backend.portalroshkabackend.Repositories.OP.EquiposRepository;
import com.backend.portalroshkabackend.Repositories.OP.TecnologiaRepository;
import com.backend.portalroshkabackend.Repositories.OP.TecnologiasEquiposRepository;
import com.backend.portalroshkabackend.Repositories.OP.UsuarioisRepository;
import com.backend.portalroshkabackend.Services.Operations.Interface.ITecnologiaEquiposService;
import com.backend.portalroshkabackend.Services.Operations.Interface.IUsuarioService;
import com.backend.portalroshkabackend.Services.Operations.Interface.Equipo.IEquipoDiaUbicacionService;
import com.backend.portalroshkabackend.Services.Operations.Interface.Equipo.IEquiposService;


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
        private final ITecnologiaEquiposService tecnologiaEquiposService;
        private final IEquipoDiaUbicacionService equipoDiaUbicacionService;

        private final Map<String, Function<Pageable, Page<Equipos>>> sortingMap;

        @Autowired
        public EquiposServiceImpl(EquiposRepository equiposRepository, ClientesRepository clientesRepository,
                        TecnologiaRepository tecnologiasRepository,
                        TecnologiasEquiposRepository tecnologiasEquiposRepository,
                        UsuarioisRepository usuarioisRepository,
                        AsignacionUsuarioRepository asignacionUsuarioRepository,
                        IUsuarioService usuarioService,
                        ITecnologiaEquiposService tecnologiaEquiposService,
                        IEquipoDiaUbicacionService equipoDiaUbicacionService) {
                this.equiposRepository = equiposRepository;
                this.clientesRepository = clientesRepository;
                this.tecnologiasRepository = tecnologiasRepository;
                this.tecnologiasEquiposRepository = tecnologiasEquiposRepository;
                this.usuarioisRepository = usuarioisRepository;
                this.asignacionUsuarioRepository = asignacionUsuarioRepository;
                this.usuarioService = usuarioService;
                this.tecnologiaEquiposService = tecnologiaEquiposService;
                this.equipoDiaUbicacionService = equipoDiaUbicacionService;

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
                dto.setNombre(e.getNombre());

                List<EquipoDiaUbicacionResponceDto> dtoList = equipoDiaUbicacionService
                                .EquipoDiaUbicacion(e.getIdEquipo());

                dto.setEquipoDiaUbicacion(dtoList);

                dto.setFechaInicio(e.getFechaInicio());
                dto.setFechaLimite(e.getFechaLimite());
                dto.setFechaCreacion(e.getFechaCreacion());
                dto.setEstado(e.getEstado());

                // --- Lider ---
                if (e.getLider() != null) {
                        Usuario u = e.getLider();
                        dto.setLider(new UsuarioisResponseDto(
                                        u.getIdUsuario(),
                                        u.getNombre(),
                                        u.getApellido(),
                                        u.getCorreo(),
                                        u.getDisponibilidad()));
                }

                // --- Cliente ---
                if (e.getCliente() != null) {
                        dto.setCliente(new ClientesResponseDto(
                                        e.getCliente().getIdCliente(),
                                        e.getCliente().getNombre()));
                }

                // --- Tecnologias ---
                List<TecnologiasDto> tecnologias = tecnologiaEquiposService.getTecnologiasByEquipo(id);
                dto.setTecnologias(tecnologias);

                // --- Users with porcentaje_trabajo ---
                List<AsignacionUsuarioEquipo> asignaciones = asignacionUsuarioRepository.findAllByEquipo_IdEquipo(id);
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

                // --- Users Not in This Team ---
                List<UsuarioisResponseDto> usuariosFueraEquipo = usuarioisRepository.findUsuariosNoEnEquipo(id)
                                .stream()
                                .map(u -> new UsuarioisResponseDto(
                                                u.getIdUsuario(),
                                                u.getNombre(),
                                                u.getApellido(),
                                                u.getCorreo(),
                                                u.getDisponibilidad()))
                                .toList();
                dto.setUsuariosNoEnEquipo(usuariosFueraEquipo);

                return dto;
        }

        // Method for show team with sort
        // SE AGREGO EL SINCHRONIZED
        @Override
        public Page<EquiposResponseDto> getTeamsSorted(Pageable pageable, String sortBy) {
                // Получаем Page<Equipos> с нужной сортировкой
                Function<Pageable, Page<Equipos>> func = sortingMap.getOrDefault(sortBy, sortingMap.get("default"));
                Page<Equipos> page = func.apply(pageable);

                // Маппим контент в DTO
                List<EquiposResponseDto> contentDto = page.getContent()
                                .stream()
                                .map(this::mapToDto)
                                .toList();

                // Создаём новый PageImpl с DTO
                return new PageImpl<>(
                                contentDto,
                                page.getPageable(),
                                page.getTotalElements());
        }

        // Map in DTO
        public EquiposResponseDto mapToDto(Equipos e) {
                EquiposResponseDto dto = new EquiposResponseDto();
                dto.setIdEquipo(e.getIdEquipo());
                dto.setNombre(e.getNombre());

                List<EquipoDiaUbicacionResponceDto> dtoList = equipoDiaUbicacionService
                                .EquipoDiaUbicacion(e.getIdEquipo());

                dto.setEquipoDiaUbicacion(dtoList);

                dto.setFechaInicio(e.getFechaInicio());
                dto.setFechaLimite(e.getFechaLimite());
                dto.setFechaCreacion(e.getFechaCreacion());
                dto.setEstado(e.getEstado());

                // Lider
                if (e.getLider() != null) {
                        Usuario u = e.getLider();
                        dto.setLider(new UsuarioisResponseDto(
                                        u.getIdUsuario(),
                                        u.getNombre(),
                                        u.getApellido(),
                                        u.getCorreo(),
                                        u.getDisponibilidad()));
                }

                // Client
                if (e.getCliente() != null) {
                        dto.setCliente(new ClientesResponseDto(
                                        e.getCliente().getIdCliente(),
                                        e.getCliente().getNombre()));
                }

                // Tecnologias
                List<TecnologiasDto> tecnologias = tecnologiaEquiposService.getTecnologiasByEquipo(e.getIdEquipo());
                dto.setTecnologias(tecnologias);

                return dto;
        }

        @Override
        public EquiposResponseDto postNewTeam(EquiposRequestDto requestDto) {
                // --- Check name ---
                if (!equiposRepository.findAllByNombre(requestDto.getNombre().trim()).isEmpty()) {
                        throw new NombreDuplicadoException("El nombre ya existe");
                }

                // ClassWithCheckingAll all = new ClassWithCheckingAll;
                // all.CheckName(requestDto.getNombre().trim());

                // --- Check Leader ---
                Usuario lider = null;
                if (requestDto.getIdLider() != null) {
                        lider = usuarioisRepository.findById(requestDto.getIdLider()).orElse(null);
                }

                // --- Check Client ---
                Clientes cliente = Optional.ofNullable(requestDto.getIdCliente())
                                .flatMap(clientesRepository::findById)
                                .orElse(null);

                // --- Check Users (validate before creating team) ---
                Map<Integer, Usuario> usuariosValidados = new HashMap<>();
                if (requestDto.getUsuarios() != null) {
                        for (UsuarioAsignacionDto uDto : requestDto.getUsuarios()) {
                                Usuario usuario = usuarioisRepository.findById(uDto.getIdUsuario())
                                                .orElseThrow(() -> new ResponseStatusException(
                                                                HttpStatus.BAD_REQUEST,
                                                                "Usuario not found: " + uDto.getIdUsuario()));

                                // Проверки обязательных полей
                                if (uDto.getPorcentajeTrabajo() == null) {
                                        throw new ResponseStatusException(
                                                        HttpStatus.BAD_REQUEST,
                                                        "PorcentajeTrabajo obligatorio para usuario "
                                                                        + usuario.getIdUsuario());
                                }
                                if (uDto.getFechaEntrada() == null) {
                                        throw new ResponseStatusException(
                                                        HttpStatus.BAD_REQUEST,
                                                        "FechaEntrada obligatoria para usuario "
                                                                        + usuario.getIdUsuario());
                                }

                                // Проверка доступности через кастомное исключение
                                if (usuario.getDisponibilidad() == null
                                                || usuario.getDisponibilidad() < uDto.getPorcentajeTrabajo()) {
                                        throw new DisponibilidadInsuficienteException(
                                                        "Usuario " + usuario.getIdUsuario() +
                                                                        " no tiene suficiente disponibilidad. Actual: "
                                                                        +
                                                                        (usuario.getDisponibilidad() == null ? 0
                                                                                        : usuario.getDisponibilidad())
                                                                        +
                                                                        ", requerido: " + uDto.getPorcentajeTrabajo());
                                }

                                usuariosValidados.put(usuario.getIdUsuario(), usuario);
                        }
                }

                // --- Create team ---
                Equipos equipo = new Equipos();
                equipo.setNombre(requestDto.getNombre().trim());
                equipo.setLider(lider);
                equipo.setCliente(cliente);
                equipo.setFechaInicio(requestDto.getFechaInicio());
                equipo.setFechaLimite(requestDto.getFechaLimite()); // может быть null
                equipo.setEstado(EstadoActivoInactivo.valueOf(requestDto.getEstado()));
                equipo.setFechaCreacion(LocalDateTime.now());

                Equipos savedEquipo = equiposRepository.save(equipo);

                // --- DTO Lider ---
                UsuarioisResponseDto liderDto = Optional.ofNullable(lider)
                                .map(l -> new UsuarioisResponseDto(l.getIdUsuario(), l.getNombre(), l.getApellido(),
                                                l.getCorreo(), l.getDisponibilidad()))
                                .orElse(null);

                // --- Tech ---
                // classPutInfoInTecnologiasEquiposRepository.putinfo(requestDto.getIdTecnologias());

                List<TecnologiasDto> tecnologias = new ArrayList<>();
                if (requestDto.getIdTecnologias() != null) {
                        for (Integer idTec : requestDto.getIdTecnologias()) {
                                Tecnologias tecnologia = tecnologiasRepository.findByIdTecnologia(idTec);
                                TecnologiasEquipos tecEquipo = new TecnologiasEquipos();
                                tecEquipo.setEquipo(savedEquipo);
                                tecEquipo.setTecnologia(tecnologia);
                                tecnologiasEquiposRepository.save(tecEquipo);
                                tecnologias.add(new TecnologiasDto(tecnologia.getIdTecnologia(),
                                                tecnologia.getNombre(), tecnologia.getDescripcion()));
                        }
                }

                // classPutInfoInasignacionUsuarioRepository.putinfo(requestDto.getUsuarios());
                // --- Assign Users ---
                List<UsuarioAsignacionDto> usuariosDto = new ArrayList<>();
                if (requestDto.getUsuarios() != null) {
                        for (UsuarioAsignacionDto uDto : requestDto.getUsuarios()) {
                                Usuario usuario = usuariosValidados.get(uDto.getIdUsuario());

                                usuario.setDisponibilidad(
                                                usuario.getDisponibilidad() - uDto.getPorcentajeTrabajo().intValue());
                                usuarioisRepository.save(usuario);

                                AsignacionUsuarioEquipo asignacion = new AsignacionUsuarioEquipo();
                                asignacion.setEquipo(savedEquipo);
                                asignacion.setUsuario(usuario);
                                asignacion.setPorcentajeTrabajo(uDto.getPorcentajeTrabajo());
                                asignacion.setFechaEntrada(uDto.getFechaEntrada());
                                asignacion.setFechaFin(uDto.getFechaFin());
                                asignacion.setFechaCreacion(LocalDateTime.now());
                                asignacionUsuarioRepository.save(asignacion);

                                usuariosDto.add(new UsuarioAsignacionDto(
                                                usuario.getIdUsuario(), usuario.getNombre(), usuario.getApellido(),
                                                usuario.getCorreo(), uDto.getPorcentajeTrabajo(),
                                                uDto.getFechaEntrada(), uDto.getFechaFin()));
                        }
                }

                // --- Final DTO ---
                EquiposResponseDto responseDto = new EquiposResponseDto();
                responseDto.setIdEquipo(savedEquipo.getIdEquipo());
                responseDto.setNombre(savedEquipo.getNombre());
                responseDto.setCliente(Optional.ofNullable(savedEquipo.getCliente())
                                .map(c -> new ClientesResponseDto(c.getIdCliente(), c.getNombre()))
                                .orElse(null));
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
                // --- load team ---
                Equipos equipo = equiposRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Equipo not found"));

                // --- update nombre ---
                String nuevoNombre = requestDto.getNombre() != null ? requestDto.getNombre().trim() : null;

                if (nuevoNombre != null && !nuevoNombre.isEmpty() && !nuevoNombre.equals(equipo.getNombre())) {
                        if (!equiposRepository.findAllByNombre(requestDto.getNombre().trim()).isEmpty()) {
                                throw new NombreDuplicadoException("El nombre ya existe");
                        }
                        equipo.setNombre(nuevoNombre);
                }

                // --- update lider ---
                if (requestDto.getIdLider() != null) {
                        Usuario nuevoLider = usuarioService.getUsuarioById(requestDto.getIdLider());
                        equipo.setLider(nuevoLider);
                } else {
                        equipo.setLider(null);
                }

                // --- update cliente ---
                if (requestDto.getIdCliente() != null) {
                        Clientes nuevoCliente = clientesRepository.findById(requestDto.getIdCliente())
                                        .orElseThrow(() -> new RuntimeException("Cliente not found"));
                        equipo.setCliente(nuevoCliente);
                } else {
                        equipo.setCliente(null);
                }

                // --- update estado y fechas ---
                Optional.ofNullable(requestDto.getFechaInicio()).ifPresent(equipo::setFechaInicio);
                equipo.setFechaLimite(requestDto.getFechaLimite()); // puede ser null
                Optional.ofNullable(requestDto.getEstado())
                                .ifPresent(e -> equipo.setEstado(EstadoActivoInactivo.valueOf(e)));

                // --- update tecnologias ---
                if (requestDto.getIdTecnologias() != null) {
                        tecnologiaEquiposService.updateTecnologiasEquipo(equipo, requestDto.getIdTecnologias());
                }

                // --- save cambios básicos ---
                equiposRepository.save(equipo);

                // --- update usuarios asignados ---
                Map<Integer, AsignacionUsuarioEquipo> actualesMap = asignacionUsuarioRepository
                                .findAllByEquipo_IdEquipo(id)
                                .stream()
                                .collect(Collectors.toMap(a -> a.getUsuario().getIdUsuario(), a -> a));

                List<UsuarioAsignacionDto> usuariosDto = new ArrayList<>();
                if (requestDto.getUsuarios() != null) {
                        for (UsuarioAsignacionDto uDto : requestDto.getUsuarios()) {
                                Usuario usuario = usuarioService.getUsuarioById(uDto.getIdUsuario());

                                // --- validaciones obligatorias ---
                                if (usuario.getDisponibilidad() < uDto.getPorcentajeTrabajo()) {
                                        throw new DisponibilidadInsuficienteException(
                                                        "Usuario " + usuario.getIdUsuario() +
                                                                        " no tiene suficiente disponibilidad. Actual: "
                                                                        +
                                                                        usuario.getDisponibilidad() + ", requerido: "
                                                                        + uDto.getPorcentajeTrabajo());
                                }

                                float viejoPorcentaje = actualesMap
                                                .getOrDefault(usuario.getIdUsuario(), new AsignacionUsuarioEquipo())
                                                .getPorcentajeTrabajo();
                                float delta = uDto.getPorcentajeTrabajo() - viejoPorcentaje;
                                usuarioService.ajustarDisponibilidadConDelta(usuario, delta);

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
                                usuariosDto.add(uDto);
                        }

                        // --- eliminar usuarios que ya no están asignados ---
                        for (AsignacionUsuarioEquipo asignacion : actualesMap.values()) {
                                if (requestDto.getUsuarios().stream()
                                                .noneMatch(u -> u.getIdUsuario()
                                                                .equals(asignacion.getUsuario().getIdUsuario()))) {
                                        Usuario usuario = asignacion.getUsuario();
                                        usuario.setDisponibilidad(usuario.getDisponibilidad()
                                                        + asignacion.getPorcentajeTrabajo());
                                        usuarioisRepository.save(usuario);
                                        asignacionUsuarioRepository.delete(asignacion);
                                }
                        }
                }

                // --- form response dto ---
                UsuarioisResponseDto liderDto = Optional.ofNullable(equipo.getLider())
                                .map(l -> new UsuarioisResponseDto(l.getIdUsuario(), l.getNombre(), l.getApellido(),
                                                l.getCorreo(), l.getDisponibilidad()))
                                .orElse(null);

                List<TecnologiasDto> tecnologias = tecnologiasEquiposRepository
                                .findAllByEquipo_IdEquipo(equipo.getIdEquipo())
                                .stream()
                                .map(te -> {
                                        Tecnologias t = te.getTecnologia();
                                        return new TecnologiasDto(t.getIdTecnologia(), t.getNombre(),
                                                        t.getDescripcion());
                                })
                                .toList();

                EquiposResponseDto responseDto = new EquiposResponseDto();
                responseDto.setIdEquipo(equipo.getIdEquipo());
                responseDto.setNombre(equipo.getNombre());
                responseDto.setCliente(Optional.ofNullable(equipo.getCliente())
                                .map(c -> new ClientesResponseDto(c.getIdCliente(), c.getNombre()))
                                .orElse(null));
                responseDto.setLider(liderDto);
                responseDto.setFechaInicio(equipo.getFechaInicio());
                responseDto.setFechaLimite(equipo.getFechaLimite());
                responseDto.setEstado(equipo.getEstado());
                responseDto.setTecnologias(tecnologias);
                responseDto.setUsuariosAsignacion(usuariosDto);
                responseDto.setFechaCreacion(equipo.getFechaCreacion());

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
