package com.backend.portalroshkabackend.Services.HumanResource;

import com.backend.portalroshkabackend.DTO.UserDto;
import com.backend.portalroshkabackend.DTO.UserInsertDto;
import com.backend.portalroshkabackend.DTO.UserUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IEmployeeService {
    Page<UserDto> getAllEmployees(Pageable pageable);
    Page<UserDto> getAllActiveEmployees(Pageable pageable);
    Page<UserDto> getAllInactiveEmployees(Pageable pageable);
    Page<UserDto> getAllEmployeesByRol(Pageable pageable);
    Page<UserDto> getAllEmployeesByTeam(Pageable pageable);
    Page<UserDto> getAllEmployeesByPosition(Pageable pageable);
    UserDto getEmployeeById(int id);
    UserDto addEmployee(UserInsertDto insertDto);
    UserDto updateEmployee(UserUpdateDto updateDto, int id);
    UserDto deleteEmployee(int id);
}
