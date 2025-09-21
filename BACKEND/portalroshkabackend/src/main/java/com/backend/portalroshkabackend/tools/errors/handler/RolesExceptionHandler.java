package com.backend.portalroshkabackend.tools.errors.handler;

import com.backend.portalroshkabackend.DTO.ErrorResponseDto;
import com.backend.portalroshkabackend.tools.errors.errorslist.roles.RoleAssignedToUsersException;
import com.backend.portalroshkabackend.tools.errors.errorslist.roles.RolesNotFoundException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(1)
public class RolesExceptionHandler extends BaseExceptionHandler{

    @ExceptionHandler(RolesNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleRolesNotFound(RolesNotFoundException ex){
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(RoleAssignedToUsersException.class)
    public ResponseEntity<ErrorResponseDto> handleRoleAssignedToUsers(RoleAssignedToUsersException ex){
        return buildError(HttpStatus.CONFLICT, ex.getMessage());
    }

}
