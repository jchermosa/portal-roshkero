package com.backend.portalroshkabackend.Services.HumanResource;

<<<<<<< HEAD
import com.backend.portalroshkabackend.DTO.th.UserByIdResponseDto;
import com.backend.portalroshkabackend.DTO.th.UserResponseDto;
import com.backend.portalroshkabackend.DTO.UserInsertDto;
import com.backend.portalroshkabackend.DTO.UserUpdateDto;
=======

import com.backend.portalroshkabackend.DTO.UsuarioDTO.UserDto;
import com.backend.portalroshkabackend.DTO.common.UserInsertDto;
import com.backend.portalroshkabackend.DTO.common.UserUpdateDto;
import com.backend.portalroshkabackend.DTO.th.employees.DefaultResponseDto;
import com.backend.portalroshkabackend.DTO.th.employees.UserByIdResponseDto;
import com.backend.portalroshkabackend.DTO.th.employees.UserResponseDto;

import com.backend.portalroshkabackend.Models.Enum.EstadoActivoInactivo;
>>>>>>> parent of dca61a3 (se elimino backend)
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IEmployeeService {
<<<<<<< HEAD
    Page<UserResponseDto> getAllEmployees(Pageable pageable);
    Page<UserResponseDto> getAllActiveEmployees(Pageable pageable);
    Page<UserResponseDto> getAllInactiveEmployees(Pageable pageable);
    Page<UserResponseDto> getAllEmployeesByRol(Pageable pageable);
    Page<UserResponseDto> getAllEmployeesByPosition(Pageable pageable);
    UserByIdResponseDto getEmployeeById(int id);
    UserResponseDto addEmployee(UserInsertDto insertDto);
    UserResponseDto updateEmployee(UserUpdateDto updateDto, int id);
    UserResponseDto deleteEmployee(int id);
=======
    DefaultResponseDto resetUserPassword(int id);
    Page<UserResponseDto> getAllEmployeesByFilters(Integer rolId, Integer cargoId, EstadoActivoInactivo estado, Pageable pageable);
    UserByIdResponseDto getEmployeeById(int id);
    UserDto getEmployeeByCedula(String cedula);
    DefaultResponseDto addEmployee(UserInsertDto insertDto);
    DefaultResponseDto updateEmployee(UserUpdateDto updateDto, int id);
    DefaultResponseDto deleteEmployee(int id);
>>>>>>> parent of dca61a3 (se elimino backend)
}
