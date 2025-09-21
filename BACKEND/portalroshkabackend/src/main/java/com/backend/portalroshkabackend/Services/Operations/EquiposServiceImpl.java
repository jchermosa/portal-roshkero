package com.backend.portalroshkabackend.Services.Operations;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

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

    private final Map<String, Function<Pageable, Page<Equipos>>> sortingMap;

    @Autowired
    public EquiposServiceImpl(EquiposRepository equiposRepository, ClientesRepository clientesRepository,
            TecnologiaRepository tecnologiasRepository, TecnologiasEquiposRepository tecnologiasEquiposRepository,
            UsuarioisRepository usuarioisRepository, AsignacionUsuarioRepository asignacionUsuarioRepository) {
        this.equiposRepository = equiposRepository;
        this.clientesRepository = clientesRepository;
        this.tecnologiasRepository = tecnologiasRepository;
        this.tecnologiasEquiposRepository = tecnologiasEquiposRepository;
        this.usuarioisRepository = usuarioisRepository;
        this.asignacionUsuarioRepository = asignacionUsuarioRepository;

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
                .orElseThrow(() -> new RuntimeException("E"));

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

        List<AsignacionUsuario> asignaciones = asignacionUsuarioRepository.findAllByEquipo_IdEquipo(id);

        List<UsuarioisResponseDto> usuariosDto = asignaciones.stream()
                .map(AsignacionUsuario::getUsuario) // достаем пользователя сразу
                .map(u -> new UsuarioisResponseDto(
                        u.getIdUsuario(),
                        u.getNombre(),
                        u.getApellido(),
                        u.getCorreo(),
                        u.getDisponibilidad()))
                .toList();

        dto.setUsuarios(usuariosDto);
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

        Usuario lider = usuarioisRepository.findById(requestDto.getIdLider())
                .orElseThrow(() -> new RuntimeException("Lider not found"));

        // Находим клиента
        Clientes cliente = clientesRepository.findById(requestDto.getIdCliente())
                .orElseThrow(() -> new RuntimeException("Cliente not found"));

        // Проверка уникальности имени команды
        List<Equipos> existentes = equiposRepository.findAllByNombre(requestDto.getNombre().trim());
        if (!existentes.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "nombre: El nombre ya existe");
        }

        // Создаем команду
        Equipos equipo = new Equipos();
        equipo.setNombre(requestDto.getNombre());
        equipo.setLider(lider);
        equipo.setFechaInicio(requestDto.getFechaInicio());
        equipo.setFechaLimite(requestDto.getFechaLimite());
        equipo.setCliente(cliente);
        equipo.setEstado(EstadoActivoInactivo.valueOf(requestDto.getEstado()));
        equipo.setFechaCreacion(new Date(System.currentTimeMillis()));
        System.out.println(equipo);
        // Сохраняем команду
        Equipos savedEquipo = equiposRepository.save(equipo);

        Usuario liderEntity = savedEquipo.getLider();

        UsuarioisResponseDto liderDto = new UsuarioisResponseDto();
        liderDto.setIdUsuario(liderEntity.getIdUsuario());
        liderDto.setNombre(liderEntity.getNombre());
        liderDto.setApellido(liderEntity.getApellido());
        liderDto.setCorreo(liderEntity.getCorreo());

        // Создаем связи с технологиями через таблицу tecnologias_equipos
        List<Tecnologias> tecnologias = new ArrayList<>();

        // Создаем связи с технологиями через таблицу tecnologias_equipos
        for (Integer idTec : requestDto.getIdTecnologias()) {
            Tecnologias tecnologia = tecnologiasRepository.findByIdTecnologia(idTec);

            TecnologiasEquipos tecEquipo = new TecnologiasEquipos();
            tecEquipo.setEquipo(savedEquipo);
            tecEquipo.setTecnologia(tecnologia);

            tecnologiasEquiposRepository.save(tecEquipo); // сохраняем связь

            tecnologias.add(tecnologia); // для ответа в DTO
        }

        // Теперь responseDto можно вернуть с полем tecnologias
        EquiposResponseDto responseDto = new EquiposResponseDto();
        responseDto.setIdEquipo(savedEquipo.getIdEquipo());
        responseDto.setNombre(savedEquipo.getNombre());
        responseDto.setCliente(savedEquipo.getCliente());
        responseDto.setLider(liderDto);
        responseDto.setFechaInicio(savedEquipo.getFechaInicio());
        responseDto.setFechaLimite(savedEquipo.getFechaLimite());
        responseDto.setEstado(savedEquipo.getEstado());
        responseDto.setTecnologias(tecnologias);

        return responseDto;
    }

    // @Override
    // public EquiposResponseDto updateTeam(int idEquipo, EquiposRequestDto
    // requestDto) {

    // Clientes cliente = clientesRepository.findById(requestDto.getIdCliente())
    // .orElseThrow(() -> new RuntimeException("Cliente not found"));

    // Equipos existingEquipo = equiposRepository.findById(idEquipo)
    // .orElseThrow(() -> new RuntimeException("Team not found"));

    // // Проверка уникальности имени среди других записей
    // Optional<Equipos> otro =
    // equiposRepository.findByNombre(requestDto.getNombre().trim());
    // if (otro.isPresent() && !otro.get().getIdEquipo().equals(idEquipo)) {
    // throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "nombre: El nombre
    // ya existe");
    // }

    // // Обновляем поля
    // existingEquipo.setNombre(requestDto.getNombre());
    // existingEquipo.setFechaInicio(requestDto.getFechaInicio());
    // existingEquipo.setFechaLimite(requestDto.getFechaLimite());
    // existingEquipo.setIdCliente(cliente);
    // existingEquipo.setEstado(EstadoActivoInactivo.valueOf(requestDto.getEstado()));

    // Equipos savedEquipo = equiposRepository.save(existingEquipo);

    // // Формируем DTO для ответа
    // EquiposResponseDto dto = new EquiposResponseDto();
    // dto.setIdEquipo(savedEquipo.getIdEquipo());
    // dto.setNombre(savedEquipo.getNombre());
    // dto.setFechaInicio(savedEquipo.getFechaInicio());
    // dto.setFechaLimite(savedEquipo.getFechaLimite());
    // dto.setCliente(savedEquipo.getCliente());
    // dto.setFechaCreacion(savedEquipo.getFechaCreacion());
    // dto.setEstado(savedEquipo.getEstado());

    // return dto;
    // }

    @Override
    public void deleteTeam(int id_equipo) {
        Equipos equipo = equiposRepository.findById(id_equipo)
                .orElseThrow(() -> new RuntimeException("Team not found"));
        equipo.setEstado(EstadoActivoInactivo.I); //
        equiposRepository.save(equipo);
    }

}
