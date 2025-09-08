package com.backend.portalroshkabackend.admin.service;

import com.backend.portalroshkabackend.admin.controller.HumanResourceController;
import com.backend.portalroshkabackend.admin.dto.UserDto;
import com.backend.portalroshkabackend.admin.dto.UserInsertDto;
import com.backend.portalroshkabackend.admin.dto.UserUpdateDto;
import com.backend.portalroshkabackend.admin.repository.HumanResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HumanResourceServiceImpl implements IHumanResourceService{
    private final HumanResourceRepository humanResourceRepository;

    @Autowired
    public HumanResourceServiceImpl(HumanResourceRepository humanResourceRepository){
        this.humanResourceRepository = humanResourceRepository;
    }

    @Override
    public List<UserDto> getAllEmployees() {
        var users = humanResourceRepository.findAll();

        return users.stream().map(user -> {
            UserDto userDto = new UserDto();

            userDto.setNombre(user.getNombre());
            userDto.setApellido(user.getApellido());
            userDto.setNro_cedula(user.getNro_cedula());
            userDto.setCorreo(user.getCorreo());
            userDto.setIdRol(user.getId_rol());
            userDto.setFecha_ingreso(user.getFecha_ingreso());
            userDto.setAntiguedad(user.getAntiguedad());
            userDto.setDiasVacaciones(user.getDias_vacaciones());
            userDto.setEstado(user.isEstado());
            userDto.setTelefono(user.getTelefono());
            userDto.setIdEquipo(user.getId_equipo());
            userDto.setIdCargo(user.getId_cargo());
            userDto.setFechaNacimiento(user.getFecha_nacimiento());
            userDto.setDiasVacacionesRestante(user.getDias_vacaciones_restante());
            userDto.setRequiereCambioContrasena(user.isRequiere_cambio_contrasena());

            return userDto;
        }).toList();
    }

    @Override
    public UserDto addEmployee(UserInsertDto insertDto) {
        return null;
    }

    @Override
    public UserDto updateEmployee(UserUpdateDto updateDto, int id) {
        return null;
    }

    @Override
    public UserDto deleteEmployee(int id) {
        return null;
    }
}
