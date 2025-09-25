package com.backend.portalroshkabackend.tools.errors.handler;

import com.backend.portalroshkabackend.DTO.common.ErrorResponseDto;
import com.backend.portalroshkabackend.tools.errors.errorslist.permisos.PermissionTypeInUseException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(1)
public class PermissionExceptionHandler extends BaseExceptionHandler{

    @ExceptionHandler(PermissionTypeInUseException.class)
    public ResponseEntity<ErrorResponseDto> handlerPermissionTypeInUse(PermissionTypeInUseException ex){
        return buildError(HttpStatus.CONFLICT, ex.getMessage());
    }

}
