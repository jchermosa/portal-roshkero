package com.backend.portalroshkabackend.admin.service;

import com.backend.portalroshkabackend.admin.dto.*;
import com.backend.portalroshkabackend.admin.repository.UserRepository;
import com.backend.portalroshkabackend.admin.repository.RequestRepository;
import com.backend.portalroshkabackend.common.model.Solicitudes;
import com.backend.portalroshkabackend.common.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public List<UserDto> getAllEmployees() {
        var users = userRepository.findAll(); // Guarda todos los usuarios en users

        return users.stream().map(user -> {
            UserDto userDto = new UserDto();

            userDto.setIdUsuario(user.getIdUsuario());
            userDto.setNombre(user.getNombre());
            userDto.setApellido(user.getApellido());
            userDto.setNroCedula(user.getNroCedula());
            userDto.setCorreo(user.getCorreo());
            userDto.setIdRol(user.getIdRol());
            userDto.setFechaIngreso(user.getFechaIngreso());
            userDto.setAntiguedad(user.getAntiguedad());
            userDto.setDiasVacaciones(user.getDiasVacaciones());
            userDto.setEstado(user.isEstado());
            userDto.setContrasena(user.getContrasena());
            userDto.setTelefono(user.getTelefono());
            userDto.setIdEquipo(user.getIdEquipo());
            userDto.setIdCargo(user.getIdCargo());
            userDto.setFechaNacimiento(user.getFechaNacimiento());
            userDto.setDiasVacacionesRestante(user.getDiasVacacionesRestante());
            userDto.setRequiereCambioContrasena(user.isRequiereCambioContrasena());

            return userDto;
        }).toList(); //Retorna una lista de dtos
    }

    @Override
    public List<UserDto> getAllActiveEmployees() {
        var users = userRepository.findAllActiveEmployees();

        return users.stream().map(user -> {
            UserDto userDto = new UserDto();

            userDto.setIdUsuario(user.getIdUsuario());
            userDto.setNombre(user.getNombre());
            userDto.setApellido(user.getApellido());
            userDto.setNroCedula(user.getNroCedula());
            userDto.setCorreo(user.getCorreo());
            userDto.setIdRol(user.getIdRol());
            userDto.setFechaIngreso(user.getFechaIngreso());
            userDto.setAntiguedad(user.getAntiguedad());
            userDto.setDiasVacaciones(user.getDiasVacaciones());
            userDto.setEstado(user.isEstado());
            userDto.setContrasena(user.getContrasena());
            userDto.setTelefono(user.getTelefono());
            userDto.setIdEquipo(user.getIdEquipo());
            userDto.setIdCargo(user.getIdCargo());
            userDto.setFechaNacimiento(user.getFechaNacimiento());
            userDto.setDiasVacacionesRestante(user.getDiasVacacionesRestante());
            userDto.setRequiereCambioContrasena(user.isRequiereCambioContrasena());

            return userDto;
        }).toList(); // Retorna una lista de empleados activos (DTOs)
    }

    @Override
    public List<UserDto> getAllInactiveEmployees() {
        var users = userRepository.findAllInactiveEmployees();

        return users.stream().map(user -> {
            UserDto userDto = new UserDto();

            userDto.setIdUsuario(user.getIdUsuario());
            userDto.setNombre(user.getNombre());
            userDto.setApellido(user.getApellido());
            userDto.setNroCedula(user.getNroCedula());
            userDto.setCorreo(user.getCorreo());
            userDto.setIdRol(user.getIdRol());
            userDto.setFechaIngreso(user.getFechaIngreso());
            userDto.setAntiguedad(user.getAntiguedad());
            userDto.setDiasVacaciones(user.getDiasVacaciones());
            userDto.setEstado(user.isEstado());
            userDto.setContrasena(user.getContrasena());
            userDto.setTelefono(user.getTelefono());
            userDto.setIdEquipo(user.getIdEquipo());
            userDto.setIdCargo(user.getIdCargo());
            userDto.setFechaNacimiento(user.getFechaNacimiento());
            userDto.setDiasVacacionesRestante(user.getDiasVacacionesRestante());
            userDto.setRequiereCambioContrasena(user.isRequiereCambioContrasena());

            return userDto;
        }).toList();
    }

    @Override
    public UserDto getEmployeeById(int id) {
        var user = userRepository.findById(id);

        UserDto userDto = new UserDto();

        userDto.setIdUsuario(user.get().getIdUsuario());
        userDto.setNombre(user.get().getNombre());
        userDto.setApellido(user.get().getApellido());
        userDto.setNroCedula(user.get().getNroCedula());
        userDto.setCorreo(user.get().getCorreo());
        userDto.setIdRol(user.get().getIdRol());
        userDto.setFechaIngreso(user.get().getFechaIngreso());
        userDto.setAntiguedad(user.get().getAntiguedad());
        userDto.setDiasVacaciones(user.get().getDiasVacaciones());
        userDto.setContrasena(user.get().getContrasena());
        userDto.setEstado(user.get().isEstado());
        userDto.setContrasena(user.get().getContrasena());
        userDto.setTelefono(user.get().getTelefono());
        userDto.setIdEquipo(user.get().getIdEquipo());
        userDto.setIdCargo(user.get().getIdCargo());
        userDto.setFechaNacimiento(user.get().getFechaNacimiento());
        userDto.setRequiereCambioContrasena(user.get().isRequiereCambioContrasena());

        return userDto;
    }

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

        savedUser = userRepository.save(savedUser);

        int id = savedUser.getIdUsuario();

        return getEmployeeById(id);
    }

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

        UserDto userDto = new UserDto();

        userDto.setIdUsuario(updatedUser.getIdUsuario());
        userDto.setNombre(updatedUser.getNombre());
        userDto.setApellido(updatedUser.getApellido());
        userDto.setNroCedula(updatedUser.getNroCedula());
        userDto.setCorreo(updatedUser.getCorreo());
        userDto.setIdRol(updatedUser.getIdRol());
        userDto.setFechaIngreso(updatedUser.getFechaIngreso());
        userDto.setAntiguedad(updatedUser.getAntiguedad());
        userDto.setDiasVacaciones(updatedUser.getDiasVacaciones());
        userDto.setEstado(updateDto.isEstado());
        userDto.setContrasena(updateDto.getContrasena());
        userDto.setTelefono(updateDto.getTelefono());
        userDto.setIdEquipo(updateDto.getIdEquipo());
        userDto.setIdCargo(updateDto.getIdCargo());
        userDto.setFechaNacimiento(updateDto.getFechaNacimiento());
        userDto.setRequiereCambioContrasena(updatedUser.isRequiereCambioContrasena());

        return userDto;
    }

    @Override
    public UserDto deleteEmployee(int id) {
        Optional<Usuario> userExists = userRepository.findById(id);

        if (userExists.isEmpty()){
            return null;
        }

        Usuario user = userExists.get();

        user.setEstado(false); // Da de baja el empleado de la base de datos

        userRepository.save(user);

        UserDto userDto = new UserDto();

        userDto.setIdUsuario(user.getIdUsuario());
        userDto.setNombre(user.getNombre());
        userDto.setApellido(user.getApellido());
        userDto.setNroCedula(user.getNroCedula());
        userDto.setCorreo(user.getCorreo());
        userDto.setIdRol(user.getIdRol());
        userDto.setFechaIngreso(user.getFechaIngreso());
        userDto.setAntiguedad(user.getAntiguedad());
        userDto.setDiasVacaciones(user.getDiasVacaciones());
        userDto.setEstado(user.isEstado());
        userDto.setContrasena(user.getContrasena());
        userDto.setTelefono(user.getTelefono());
        userDto.setIdEquipo(user.getIdEquipo());
        userDto.setIdCargo(user.getIdCargo());
        userDto.setFechaNacimiento(user.getFechaNacimiento());
        userDto.setRequiereCambioContrasena(user.isRequiereCambioContrasena());

        return userDto;
    }

    @Override
    public List<RequestDto> getAllRequests() {
        List<Solicitudes> requests = requestRepository.findAll();
        return requests.stream().map(request -> {
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
        }).toList(); // Retorna todas las solicitudes (DTOs)
    }

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

    @Override
    public RequestDto addNewRequestType() {
        return null;
    }

}
