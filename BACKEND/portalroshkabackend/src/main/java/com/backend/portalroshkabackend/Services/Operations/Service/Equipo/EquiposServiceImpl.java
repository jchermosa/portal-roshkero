package com.backend.portalroshkabackend.Services.Operations.Service.Equipo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.backend.portalroshkabackend.DTO.Operationes.EquiposRequestDto;
import com.backend.portalroshkabackend.DTO.Operationes.EquiposResponseDto;
import com.backend.portalroshkabackend.DTO.Operationes.TecnologiasDto;
import com.backend.portalroshkabackend.DTO.Operationes.UsuarioAsignacionDto;
import com.backend.portalroshkabackend.DTO.Operationes.UsuarioisResponseDto;
import com.backend.portalroshkabackend.Models.Equipos;
import com.backend.portalroshkabackend.Models.Usuario;
import com.backend.portalroshkabackend.Models.Enum.EstadoActivoInactivo;
import com.backend.portalroshkabackend.Repositories.OP.ClientesRepository;
import com.backend.portalroshkabackend.Repositories.OP.EquiposRepository;
import com.backend.portalroshkabackend.Services.Operations.Interface.ITecnologiaEquiposService;
import com.backend.portalroshkabackend.Services.Operations.Interface.IUsuarioService;
import com.backend.portalroshkabackend.Services.Operations.Interface.Equipo.IEquiposService;
import com.backend.portalroshkabackend.Services.Operations.Service.UsuarioServiceImpl;
import com.backend.portalroshkabackend.tools.mapper.EquiposMapper;
import com.backend.portalroshkabackend.tools.validator.TeamValidator;

@Service("operationsEquiposService")
@Validated
public class EquiposServiceImpl implements IEquiposService {

        private final EquiposRepository equiposRepository;
        private final ClientesRepository clientesRepository;
        private final IUsuarioService usuarioService;
        private final ITecnologiaEquiposService tecnologiaEquiposService;
        private final TeamValidator teamValidator;
        private final EquiposMapper equiposMapper;
        private final UsuarioServiceImpl UsuarioServiceImpl;

        // сортировка страниц
        private final Map<String, Function<Pageable, Page<Equipos>>> sortingMap;

        @Autowired
        public EquiposServiceImpl(
                        EquiposRepository equiposRepository,
                        ClientesRepository clientesRepository,
                        IUsuarioService usuarioService,
                        ITecnologiaEquiposService tecnologiaEquiposService,
                        TeamValidator teamValidator,
                        EquiposMapper equiposMapper,
                        UsuarioServiceImpl UsuarioServiceImpl) {
                this.equiposRepository = equiposRepository;
                this.clientesRepository = clientesRepository;
                this.usuarioService = usuarioService;
                this.tecnologiaEquiposService = tecnologiaEquiposService;
                this.teamValidator = teamValidator;
                this.equiposMapper = equiposMapper;
                this.UsuarioServiceImpl = UsuarioServiceImpl;

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
                return equiposMapper.getTeam(e);
        }

        // Method for show team with sort
        // SE AGREGO EL SINCHRONIZED
        @Override
        public Page<EquiposResponseDto> getTeamsSorted(Pageable pageable, String sortBy) {
                // Получаем Page<Equipos> с нужной сортировкой
                Function<Pageable, Page<Equipos>> func = sortingMap.getOrDefault(sortBy, sortingMap.get("default"));
                Page<Equipos> page = func.apply(pageable);

                // Маппим контент в DTO через маппер
                List<EquiposResponseDto> contentDto = page.getContent()
                                .stream()
                                .map(equiposMapper::getAllTeams)
                                .toList();

                // Создаём новый PageImpl с DTO
                return new PageImpl<>(
                                contentDto,
                                page.getPageable(),
                                page.getTotalElements());
        }

        @Override
        public void postNewTeam(EquiposRequestDto requestDto) {
                // --- 1. Валидация уникального имени и дат команды ---
                teamValidator.validateUniqueName(requestDto.getNombre());
                teamValidator.validateFechaInicioAntesDeLimite(requestDto);

                // --- 2. Валидация лидера ---
                Usuario lider = teamValidator.validateLeader(requestDto.getIdLider());

                // --- 3. Создание команды ---
                Equipos equipo = new Equipos();
                equipo.setNombre(requestDto.getNombre().trim());
                equipo.setLider(lider);
                equipo.setCliente(Optional.ofNullable(requestDto.getIdCliente())
                                .flatMap(clientesRepository::findById)
                                .orElse(null));
                equipo.setFechaInicio(requestDto.getFechaInicio());
                equipo.setFechaLimite(requestDto.getFechaLimite());
                equipo.setEstado(EstadoActivoInactivo.valueOf(requestDto.getEstado()));
                equipo.setFechaCreacion(LocalDateTime.now());
                Equipos savedEquipo = equiposRepository.save(equipo);

                // --- 4. Сохранение технологий команды ---
                tecnologiaEquiposService.updateTecnologiasEquipo(savedEquipo, requestDto.getIdTecnologias());

                // --- 5. Валидация только дат пользователей ---
                if (requestDto.getUsuarios() != null && !requestDto.getUsuarios().isEmpty()) {
                        // Валидация дат пользователей
                        teamValidator.validateFechasUsuarios(requestDto.getUsuarios());
                }
                // Назначение пользователей на команду и проверку процентов у них
                usuarioService.assignUsers(savedEquipo, requestDto.getUsuarios());
        }

        @Override
        public void updateTeam(Integer id, EquiposRequestDto requestDto) {
                // Получаем команду
                Equipos equipo = equiposRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Equipo not found"));

                // Обновление имени с валидацией уникальности
                String nuevoNombre = Optional.ofNullable(requestDto.getNombre())
                                .map(String::trim)
                                .orElse(null);
                if (nuevoNombre != null && !nuevoNombre.equals(equipo.getNombre())) {
                        teamValidator.validateUniqueName(nuevoNombre);
                        equipo.setNombre(nuevoNombre);
                }

                // Валидация дат команды
                teamValidator.validateFechaInicioAntesDeLimite(requestDto);

                // Обновление лидера и клиента
                equipo.setLider(teamValidator.validateLeader(requestDto.getIdLider()));
                equipo.setCliente(Optional.ofNullable(requestDto.getIdCliente())
                                .flatMap(clientesRepository::findById)
                                .orElse(null));

                // Обновление дат и статуса
                Optional.ofNullable(requestDto.getFechaInicio()).ifPresent(equipo::setFechaInicio);
                equipo.setFechaLimite(requestDto.getFechaLimite());
                Optional.ofNullable(requestDto.getEstado())
                                .ifPresent(e -> equipo.setEstado(EstadoActivoInactivo.valueOf(e)));

                // Технологии
                if (requestDto.getIdTecnologias() != null) {
                        tecnologiaEquiposService.updateTecnologiasEquipo(equipo,
                                        requestDto.getIdTecnologias());
                }

                // Validate users dates
                if (requestDto.getUsuarios() != null && !requestDto.getUsuarios().isEmpty()) {
                        teamValidator.validateFechasUsuarios(requestDto.getUsuarios());
                }
                usuarioService.updateUsers(equipo, requestDto.getUsuarios());
                equiposRepository.save(equipo);

        }

        @Override
        public void deleteTeam(int id_equipo) {
                Equipos equipo = equiposRepository.findById(id_equipo)
                                .orElseThrow(() -> new RuntimeException("Team not found"));
                equipo.setEstado(EstadoActivoInactivo.I); //
                equiposRepository.save(equipo);
        }

}
