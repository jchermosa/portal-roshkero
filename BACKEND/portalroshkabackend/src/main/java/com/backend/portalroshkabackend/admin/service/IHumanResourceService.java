package com.backend.portalroshkabackend.admin.service;

import com.backend.portalroshkabackend.admin.dto.*;

import java.util.List;

public interface IHumanResourceService {

    List<UserDto> getAllEmployees();
    List<UserDto> getAllActiveEmployees();
    List<UserDto> getAllInactiveEmployees();
    UserDto getEmployeeById(int id);
    UserDto addEmployee(UserInsertDto insertDto);
    UserDto updateEmployee(UserUpdateDto updateDto, int id);
    UserDto deleteEmployee(int id);

    List<RequestDto> getAllRequests();
    boolean acceptRequest(int idRequest);
    boolean rejectRequest(int idRequest, RequestRejectedDto rejectedDto);
    RequestDto addNewRequestType();
}
