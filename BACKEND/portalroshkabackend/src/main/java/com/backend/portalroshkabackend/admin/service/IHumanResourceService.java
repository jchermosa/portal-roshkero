package com.backend.portalroshkabackend.admin.service;

import com.backend.portalroshkabackend.admin.dto.UserDto;
import com.backend.portalroshkabackend.admin.dto.UserInsertDto;
import com.backend.portalroshkabackend.admin.dto.UserUpdateDto;

import java.util.List;

public interface IHumanResourceService {

    List<UserDto> getAllEmployees();
    UserDto addEmployee(UserInsertDto insertDto);
    UserDto updateEmployee(UserUpdateDto updateDto, int id);
    UserDto deleteEmployee(int id);
}
