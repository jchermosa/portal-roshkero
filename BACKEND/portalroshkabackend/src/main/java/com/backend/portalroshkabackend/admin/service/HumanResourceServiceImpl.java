package com.backend.portalroshkabackend.admin.service;

import com.backend.portalroshkabackend.admin.dto.UserDto;
import com.backend.portalroshkabackend.admin.dto.UserInsertDto;
import com.backend.portalroshkabackend.admin.dto.UserUpdateDto;
import com.backend.portalroshkabackend.admin.repository.HumanResourceRepository;
import com.backend.portalroshkabackend.common.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HumanResourceServiceImpl implements IHumanResourceService{
    private final HumanResourceRepository humanResourceRepository;

    @Autowired
    public HumanResourceServiceImpl(HumanResourceRepository humanResourceRepository){
        this.humanResourceRepository = humanResourceRepository;
    }

    @Override
    public List<UserDto> getAllEmployees() {
        var users = humanResourceRepository.findAll(); // Guarda todos los usuarios en users

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
        var users = humanResourceRepository.findAllActiveEmployees();

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
    public UserDto getEmployeeById(int id) {
        var user = humanResourceRepository.findById(id);

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

        Usuario savedUser = humanResourceRepository.save(user);

        savedUser = humanResourceRepository.save(savedUser);

        int id = savedUser.getIdUsuario();
        System.out.println("Id del usuario recien creado: " + id);

        return getEmployeeById(id);
    }

    @Override
    public UserDto updateEmployee(UserUpdateDto updateDto, int id) {
        Optional<Usuario> userExists = humanResourceRepository.findById(id);


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

        Usuario updatedUser = humanResourceRepository.save(user);

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
        Optional<Usuario> userExists = humanResourceRepository.findById(id);

        if (userExists.isEmpty()){
            return null;
        }

        Usuario user = userExists.get();

        user.setEstado(false); // Da de baja el empleado de la base de datos

        humanResourceRepository.save(user);

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

}
