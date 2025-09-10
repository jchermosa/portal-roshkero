package com.backend.portalroshkabackend.Services;

import com.backend.portalroshkabackend.DTO.PositionDto;
import com.backend.portalroshkabackend.DTO.PositionInsertDto;
import com.backend.portalroshkabackend.DTO.PositionUpdateDto;
import com.backend.portalroshkabackend.DTO.RequestDto;
import com.backend.portalroshkabackend.DTO.RequestRejectedDto;
import com.backend.portalroshkabackend.DTO.UserDto;
import com.backend.portalroshkabackend.DTO.UserInsertDto;
import com.backend.portalroshkabackend.DTO.UserUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IHumanResourceService {
    // ------ Employees ------
    Page<UserDto> getAllEmployees(Pageable pageable);
    Page<UserDto> getAllActiveEmployees(Pageable pageable);
    Page<UserDto> getAllInactiveEmployees(Pageable pageable);
    UserDto getEmployeeById(int id);
    UserDto addEmployee(UserInsertDto insertDto);
    UserDto updateEmployee(UserUpdateDto updateDto, int id);
    UserDto deleteEmployee(int id);

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
