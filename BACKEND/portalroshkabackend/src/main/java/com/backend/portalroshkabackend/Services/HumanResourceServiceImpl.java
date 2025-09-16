package com.backend.portalroshkabackend.Services;

import com.backend.portalroshkabackend.DTO.PositionDto;
import com.backend.portalroshkabackend.DTO.PositionInsertDto;
import com.backend.portalroshkabackend.DTO.PositionUpdateDto;
import com.backend.portalroshkabackend.DTO.RequestDto;
import com.backend.portalroshkabackend.DTO.RequestRejectedDto;
import com.backend.portalroshkabackend.DTO.UserDto;
import com.backend.portalroshkabackend.DTO.UserInsertDto;
import com.backend.portalroshkabackend.DTO.UserUpdateDto;
import com.backend.portalroshkabackend.Models.Solicitudes;
import com.backend.portalroshkabackend.Models.Usuario;
import com.backend.portalroshkabackend.Repositories.CargosRepository;
import com.backend.portalroshkabackend.Repositories.RequestRepository;
import com.backend.portalroshkabackend.Repositories.UserRepository;
import com.backend.portalroshkabackend.Models.Cargos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class HumanResourceServiceImpl implements IHumanResourceService{
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final CargosRepository cargosRepository;

    @Autowired
    public HumanResourceServiceImpl(UserRepository userRepository,
                                    RequestRepository requestRepository,
                                    CargosRepository cargosRepository){
        this.userRepository = userRepository;
        this.requestRepository = requestRepository;
        this.cargosRepository = cargosRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public Page<UserDto> getAllEmployees(Pageable pageable) {
        Page<Usuario> users = userRepository.findAll(pageable); // Guarda todos los usuarios en users

        return users.map(this::mapToUserDto); //Retorna una lista de dtos
    }

    // @Transactional(readOnly = true)
    // @Override
    // public Page<UserDto> getAllActiveEmployees(Pageable pageable) {
    //     Page<Usuario> users = userRepository.findAllActiveEmployees(pageable);

    //     return users.map(this::mapToUserDto); // Retorna una lista de empleados activos (DTOs)
    // }

    // @Transactional(readOnly = true)
    // @Override
    // public Page<UserDto> getAllInactiveEmployees(Pageable pageable) {
    //     Page<Usuario> users = userRepository.findAllInactiveEmployees(pageable);

    //     return users.map(this::mapToUserDto); // Retorna la lista de empleados inactivos
    // }

    @Override
    public Page<UserDto> getAllEmployeesByRol(Pageable pageable) {
        Page<Usuario> users = userRepository.findAllByOrderByIdRolAsc(pageable);

        return users.map(this::mapToUserDto);
    }

    // @Override
    // public Page<UserDto> getAllEmployeesByTeam(Pageable pageable) {
    //     // Page<Usuario> users = userRepository.findAllByOrderByIdEquipoAsc(pageable);

    //     return users.map(this::mapToUserDto);
    // }

    @Override
    public Page<UserDto> getAllEmployeesByPosition(Pageable pageable) {
        Page<Usuario> users = userRepository.findAllByOrderByIdCargoAsc(pageable);

        return users.map(this::mapToUserDto);
    }

    @Transactional(readOnly = true)
    @Override
    public UserDto getEmployeeById(int id) {
        var user = userRepository.findById(id);

        return user.map(this::mapToUserDto).orElse(null); //Si no existe usuario con esa id retorna null, sino retorna un Dto

    }

    @Transactional
    @Override
    public UserDto addEmployee(UserInsertDto insertDto) {

        Usuario user = new Usuario();
        user.setNombre(insertDto.getNombre());
        user.setApellido(insertDto.getApellido());
        user.setNroCedula(insertDto.getNroCedula());
        user.setCorreo(insertDto.getCorreo());
        // user.setIdRol(insertDto.getIdRol());
        user.setFechaIngreso(insertDto.getFechaIngreso());
        // user.setEstado(insertDto.isEstado());
        user.setContrasena(insertDto.getContrasena());
        user.setTelefono(insertDto.getTelefono());
        // user.setIdEquipo(insertDto.getIdEquipo());
        // user.setIdCargo(insertDto.getIdCargo());
        user.setFechaNacimiento(insertDto.getFechaNacimiento());
        // user.setRequiereCambioContrasena(insertDto.isRequiere_cambio_contrasena());

        Usuario savedUser = userRepository.save(user);

        int id = savedUser.getIdUsuario();

        return getEmployeeById(id);
    }

    @Transactional
    @Override
    public UserDto updateEmployee(UserUpdateDto updateDto, int id) {
        Optional<Usuario> userExists = userRepository.findById(id);


        if (userExists.isEmpty()) return null;

        Usuario user = userExists.get();

        user.setNombre(updateDto.getNombre());
        user.setApellido(updateDto.getApellido());
        user.setNroCedula(updateDto.getNroCedula());
        user.setCorreo(updateDto.getCorreo());
        // user.setIdRol(updateDto.getIdRol());
        user.setFechaIngreso(updateDto.getFechaIngreso());
        // user.setEstado(updateDto.isEstado());
        user.setContrasena(updateDto.getContrasena());
        user.setTelefono(updateDto.getTelefono());
        // user.setIdEquipo(updateDto.getIdEquipo());
        // user.setIdCargo(updateDto.getIdCargo());
        user.setFechaNacimiento(updateDto.getFechaNacimiento());

        Usuario updatedUser = userRepository.save(user);

        return mapToUserDto(updatedUser);
    }

    @Transactional
    @Override
    public UserDto deleteEmployee(int id) {
        Optional<Usuario> userExists = userRepository.findById(id);

        // if (userExists.isEmpty() || userExists.get().isEstado() == false){
        //     return null;
        // }

        Usuario user = userExists.get();

        // user.setEstado(false); // Da de baja el empleado de la base de datos

        userRepository.save(user);

        return mapToUserDto(user);
    }

    // ----------------- Request -----------------

    @Transactional(readOnly = true)
    @Override
    public Page<RequestDto> getAllRequests(Pageable pageable) {
        Page<Solicitudes> requests = requestRepository.findAll(pageable);
        return requests.map(this::mapToRequestDto); // Retorna todas las solicitudes (DTOs)
    }

    @Transactional
    @Override
    public boolean acceptRequest(int idRequest) {
        var request = requestRepository.findById(idRequest);

        if (request.isEmpty()){
            return false;
        }
        // Si se acepta la solicitud, rechazado y estado de la solicitu se setea a false
        // request.get().setRechazado(false);
        // request.get().setEstado(false);

        return true;
    }

    @Transactional
    @Override
    public boolean rejectRequest(int idRequest, RequestRejectedDto rejectedDto) {
        var request = requestRepository.findById(idRequest);

        if (request.isEmpty()){
            return false;
        }

        // request.get().setRechazado(true); // Setea la solicitud como rechazada
        request.get().setComentario(rejectedDto.getComentario());

        requestRepository.save(request.get());

        return true;
    }

    @Transactional
    @Override
    public RequestDto addNewRequestType() {
        return null;
    }


    // ----------------- Position -----------------

    @Transactional(readOnly = true)
    @Override
    public Page<PositionDto> getAllPositions(Pageable pageable) {
        Page<Cargos> positions = cargosRepository.findAll(pageable);

        return positions.map(this::mapToPositionDto);
    }

    @Transactional(readOnly = true)
    @Override
    public PositionDto getPositionById(int id) {
        var position =  cargosRepository.findById(id);

        return position.map(this::mapToPositionDto).orElse(null);
    }

    @Transactional
    @Override
    public PositionDto addPosition(PositionInsertDto positionInsertDto) {
        Cargos position = new Cargos();

        position.setNombre(positionInsertDto.getNombre());

        Cargos savedPosition = cargosRepository.save(position);

        return mapToPositionDto(savedPosition);
    }

    @Transactional
    @Override
    public PositionDto updatePosition(PositionUpdateDto positionUpdateDto, int id) {
        Optional<Cargos> positionExists = cargosRepository.findById(id);

        if (positionExists.isEmpty()) return null;

        Cargos position = positionExists.get();

        position.setNombre(positionUpdateDto.getNombre());

        Cargos updatedPosition = new Cargos();

        return mapToPositionDto(updatedPosition);
    }

    @Transactional
    @Override
    public PositionDto deletePosition(int id) {
        Optional<Cargos> positionExists = cargosRepository.findById(id);

        if (positionExists.isEmpty()){ // Falta validar si el cargo ya esta eliminado, para no volver a eliminar y retornar un null
            return null;
        }

        Cargos position  = positionExists.get();

        // Aca se daria de baja el cargo, ej: position.setEstado(false);

        cargosRepository.save(position);

        return mapToPositionDto(position);
    }

    private UserDto mapToUserDto(Usuario user) {
        UserDto dto = new UserDto();
        dto.setIdUsuario(user.getIdUsuario());
        dto.setNombre(user.getNombre());
        dto.setApellido(user.getApellido());
        dto.setNroCedula(user.getNroCedula());
        dto.setCorreo(user.getCorreo());
        // dto.setIdRol(user.getIdRol());
        dto.setFechaIngreso(user.getFechaIngreso());
        dto.setAntiguedad(user.getAntiguedad());


        dto.setDiasVacaciones(user.getDiasVacaciones());
        // dto.setEstado(user.isEstado());
        dto.setContrasena(user.getContrasena());
        dto.setTelefono(user.getTelefono());
        // dto.setIdEquipo(user.getIdEquipo());
        // dto.setIdCargo(user.getIdCargo());
        dto.setFechaNacimiento(user.getFechaNacimiento());
        dto.setDiasVacacionesRestante(user.getDiasVacacionesRestante());
        dto.setRequiereCambioContrasena(user.isRequiereCambioContrasena());
        return dto;
    }

    private RequestDto mapToRequestDto(Solicitudes request){
        RequestDto requestDto = new RequestDto();

        requestDto.setIdSolicitud(request.getIdSolicitud());
        requestDto.setFechaInicio(request.getFechaInicio());
        requestDto.setFechaFin(request.getFechaFin());
        requestDto.setEstado(request.getEstado());
        requestDto.setIdUsuario(request.getIdUsuario());
        requestDto.setCantidadDias(request.getCantidadDias());
        requestDto.setNumeroAprobaciones(request.getNumeroAprobaciones());
        requestDto.setComentario(request.getComentario());
        // requestDto.setRechazado(request.isRechazado());

        return requestDto;
    }

    private PositionDto mapToPositionDto(Cargos position){
        PositionDto positionDto = new PositionDto();

        positionDto.setIdCargo(position.getIdCargo());
        positionDto.setNombre(position.getNombre());

        return positionDto;
    }

}