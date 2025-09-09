package com.backend.portalroshkabackend.admin.service;

import com.backend.portalroshkabackend.admin.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IHumanResourceService {

    Page<UserDto> getAllEmployees(Pageable pageable);
    Page<UserDto> getAllActiveEmployees(Pageable pageable);
    Page<UserDto> getAllInactiveEmployees(Pageable pageable);
    UserDto getEmployeeById(int id);
    UserDto addEmployee(UserInsertDto insertDto);
    UserDto updateEmployee(UserUpdateDto updateDto, int id);
    UserDto deleteEmployee(int id);

    Page<RequestDto> getAllRequests(Pageable pageable);
    boolean acceptRequest(int idRequest);
    boolean rejectRequest(int idRequest, RequestRejectedDto rejectedDto);
    RequestDto addNewRequestType();
}
