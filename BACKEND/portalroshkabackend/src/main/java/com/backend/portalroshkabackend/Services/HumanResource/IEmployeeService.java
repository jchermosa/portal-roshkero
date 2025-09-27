package com.backend.portalroshkabackend.Services.HumanResource;


import com.backend.portalroshkabackend.DTO.UsuarioDTO.UserDto;
import com.backend.portalroshkabackend.DTO.common.UserInsertDto;
import com.backend.portalroshkabackend.DTO.common.UserUpdateDto;
import com.backend.portalroshkabackend.DTO.th.employees.DefaultResponseDto;
import com.backend.portalroshkabackend.DTO.th.employees.UserByIdResponseDto;
import com.backend.portalroshkabackend.DTO.th.employees.UserResponseDto;

import com.backend.portalroshkabackend.Models.Enum.EstadoActivoInactivo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IEmployeeService {
    DefaultResponseDto resetUserPassword(int id);
    Page<UserResponseDto> getAllEmployeesByFilters(Integer rolId, Integer cargoId, EstadoActivoInactivo estado, Pageable pageable);
    UserByIdResponseDto getEmployeeById(int id);
    UserDto getEmployeeByCedula(String cedula);
    DefaultResponseDto addEmployee(UserInsertDto insertDto);
    DefaultResponseDto updateEmployee(UserUpdateDto updateDto, int id);
    DefaultResponseDto deleteEmployee(int id);
}
