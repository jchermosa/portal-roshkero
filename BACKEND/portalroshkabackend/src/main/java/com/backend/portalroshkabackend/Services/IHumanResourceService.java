package com.backend.portalroshkabackend.Services;

import com.backend.portalroshkabackend.DTO.PositionDto;
import com.backend.portalroshkabackend.DTO.PositionInsertDto;
import com.backend.portalroshkabackend.DTO.PositionUpdateDto;
import com.backend.portalroshkabackend.DTO.RequestDto;
import com.backend.portalroshkabackend.DTO.RequestRejectedDto;
import com.backend.portalroshkabackend.DTO.th.UserResponseDto;
import com.backend.portalroshkabackend.DTO.UserInsertDto;
import com.backend.portalroshkabackend.DTO.UserUpdateDto;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IHumanResourceService {
    // ------ Employees ------
    Page<UserResponseDto> getAllEmployees(Pageable pageable);
    Page<UserResponseDto> getAllActiveEmployees(Pageable pageable);
    Page<UserResponseDto> getAllInactiveEmployees(Pageable pageable);
    Page<UserResponseDto> getAllEmployeesByRol(Pageable pageable);
    Page<UserResponseDto> getAllEmployeesByTeam(Pageable pageable);
    Page<UserResponseDto> getAllEmployeesByPosition(Pageable pageable);
    UserResponseDto getEmployeeById(int id);
    UserResponseDto addEmployee(UserInsertDto insertDto);
    UserResponseDto updateEmployee(UserUpdateDto updateDto, int id);
    UserResponseDto deleteEmployee(int id);

    // ------ Requests ------
    Page<RequestDto> getAllRequests(Pageable pageable);
    boolean acceptRequest(int idRequest);
    boolean rejectRequest(int idRequest, RequestRejectedDto rejectedDto);
    RequestDto addNewRequestType();

    // ------ Positions ------
    Page<PositionDto> getAllPositions(Pageable pageable);
    PositionDto getPositionById(int id);
    PositionDto addPosition(PositionInsertDto positionInsertDto);
    PositionDto updatePosition(PositionUpdateDto positionUpdateDto, int id);
    PositionDto deletePosition(int id);
}
