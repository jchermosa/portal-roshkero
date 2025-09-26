package com.backend.portalroshkabackend.Services.HumanResource;

import com.backend.portalroshkabackend.DTO.common.UserInsertDto;
import com.backend.portalroshkabackend.DTO.common.UserUpdateDto;
import com.backend.portalroshkabackend.DTO.th.employees.DefaultResponseDto;
import com.backend.portalroshkabackend.DTO.th.employees.UserByIdResponseDto;
import com.backend.portalroshkabackend.DTO.th.employees.UserResponseDto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IEmployeeService {
    DefaultResponseDto resetUserPassword(int id);
    Page<UserResponseDto> getAllEmployees(Pageable pageable);
    Page<UserResponseDto> getAllActiveEmployees(Pageable pageable);
    Page<UserResponseDto> getAllInactiveEmployees(Pageable pageable);
    Page<UserResponseDto> getAllEmployeesByRol(Pageable pageable);
    Page<UserResponseDto> getAllEmployeesByPosition(Pageable pageable);
    UserByIdResponseDto getEmployeeById(int id);
    DefaultResponseDto addEmployee(UserInsertDto insertDto);
    DefaultResponseDto updateEmployee(UserUpdateDto updateDto, int id);
    DefaultResponseDto deleteEmployee(int id);
}
