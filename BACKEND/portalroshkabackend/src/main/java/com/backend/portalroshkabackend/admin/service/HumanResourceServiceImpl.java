package com.backend.portalroshkabackend.admin.service;

import com.backend.portalroshkabackend.admin.dto.*;
import com.backend.portalroshkabackend.admin.repository.UserRepository;
import com.backend.portalroshkabackend.admin.repository.RequestRepository;
import com.backend.portalroshkabackend.common.model.Solicitudes;
import com.backend.portalroshkabackend.common.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class HumanResourceServiceImpl implements IHumanResourceService{
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;

    @Autowired
    public HumanResourceServiceImpl(UserRepository userRepository,
                                    RequestRepository requestRepository){
        this.userRepository = userRepository;
        this.requestRepository = requestRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserDto> getAllEmployees() {
        var users = userRepository.findAll(); // Guarda todos los usuarios en users

        return users.stream().map(this::mapToUserDto).toList(); //Retorna una lista de dtos
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserDto> getAllActiveEmployees() {
        var users = userRepository.findAllActiveEmployees();

        return users.stream().map(this::mapToUserDto).toList(); // Retorna una lista de empleados activos (DTOs)
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserDto> getAllInactiveEmployees() {
        var users = userRepository.findAllInactiveEmployees();

        return users.stream().map(this::mapToUserDto).toList(); // Retorna la lista de empleados inactivos
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
        user.setIdRol(insertDto.getIdRol());
        user.setFechaIngreso(insertDto.getFechaIngreso());
        user.setEstado(insertDto.isEstado());
        user.setContrasena(insertDto.getContrasena());
        user.setTelefono(insertDto.getTelefono());
        user.setIdEquipo(insertDto.getIdEquipo());
        user.setIdCargo(insertDto.getIdCargo());
        user.setFechaNacimiento(insertDto.getFechaNacimiento());
        user.setRequiereCambioContrasena(insertDto.isRequiere_cambio_contrasena());

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
        user.setIdRol(updateDto.getIdRol());
        user.setFechaIngreso(updateDto.getFechaIngreso());
        user.setEstado(updateDto.isEstado());
        user.setContrasena(updateDto.getContrasena());
        user.setTelefono(updateDto.getTelefono());
        user.setIdEquipo(updateDto.getIdEquipo());
        user.setIdCargo(updateDto.getIdCargo());
        user.setFechaNacimiento(updateDto.getFechaNacimiento());

        Usuario updatedUser = userRepository.save(user);

        return mapToUserDto(updatedUser);
    }

    @Transactional
    @Override
    public UserDto deleteEmployee(int id) {
        Optional<Usuario> userExists = userRepository.findById(id);

        if (userExists.isEmpty() || userExists.get().isEstado() == false){
            return null;
        }

        Usuario user = userExists.get();

        user.setEstado(false); // Da de baja el empleado de la base de datos

        userRepository.save(user);

        return mapToUserDto(user);
    }

    @Transactional(readOnly = true)
    @Override
    public List<RequestDto> getAllRequests() {
        List<Solicitudes> requests = requestRepository.findAll();
        return requests.stream().map(this::mapToRequestDto).toList(); // Retorna todas las solicitudes (DTOs)
    }

    @Transactional
    @Override
    public boolean acceptRequest(int idRequest) {
        var request = requestRepository.findById(idRequest);

        if (request.isEmpty()){
            return false;
        }
        // Si se acepta la solicitud, rechazado y estado de la solicitu se setea a false
        request.get().setRechazado(false);
        request.get().setEstado(false);

        return true;
    }

    @Transactional
    @Override
    public boolean rejectRequest(int idRequest, RequestRejectedDto rejectedDto) {
        var request = requestRepository.findById(idRequest);

        if (request.isEmpty()){
            return false;
        }

        request.get().setRechazado(true); // Setea la solicitud como rechazada
        request.get().setComentario(rejectedDto.getComentario());

        requestRepository.save(request.get());

        return true;
    }

    @Transactional
    @Override
    public RequestDto addNewRequestType() {
        return null;
    }

    private UserDto mapToUserDto(Usuario user) {
        UserDto dto = new UserDto();
        dto.setIdUsuario(user.getIdUsuario());
        dto.setNombre(user.getNombre());
        dto.setApellido(user.getApellido());
        dto.setNroCedula(user.getNroCedula());
        dto.setCorreo(user.getCorreo());
        dto.setIdRol(user.getIdRol());
        dto.setFechaIngreso(user.getFechaIngreso());
        dto.setAntiguedad(user.getAntiguedad());
        dto.setDiasVacaciones(user.getDiasVacaciones());
        dto.setEstado(user.isEstado());
        dto.setContrasena(user.getContrasena());
        dto.setTelefono(user.getTelefono());
        dto.setIdEquipo(user.getIdEquipo());
        dto.setIdCargo(user.getIdCargo());
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
        requestDto.setEstado(request.isEstado());
        requestDto.setIdUsuario(request.getIdUsuario());
        requestDto.setCantidadDias(request.getCantidadDias());
        requestDto.setNumeroAprobaciones(request.getNumeroAprobaciones());
        requestDto.setComentario(request.getComentario());
        requestDto.setRechazado(request.isRechazado());

        return requestDto;
    }

}
