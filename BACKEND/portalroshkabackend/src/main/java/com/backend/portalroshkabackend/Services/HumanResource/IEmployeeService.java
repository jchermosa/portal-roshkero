package com.backend.portalroshkabackend.Services.HumanResource;

import com.backend.portalroshkabackend.DTO.th.UserByIdResponseDto;
import com.backend.portalroshkabackend.DTO.th.UserResponseDto;
import com.backend.portalroshkabackend.DTO.UserInsertDto;
import com.backend.portalroshkabackend.DTO.UserUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IEmployeeService {
    Page<UserResponseDto> getAllEmployees(Pageable pageable);
    Page<UserResponseDto> getAllActiveEmployees(Pageable pageable);
    Page<UserResponseDto> getAllInactiveEmployees(Pageable pageable);
    Page<UserResponseDto> getAllEmployeesByRol(Pageable pageable);
    Page<UserResponseDto> getAllEmployeesByPosition(Pageable pageable);
    UserByIdResponseDto getEmployeeById(int id);
    UserResponseDto addEmployee(UserInsertDto insertDto);
    UserResponseDto updateEmployee(UserUpdateDto updateDto, int id);
    UserResponseDto deleteEmployee(int id);
}
