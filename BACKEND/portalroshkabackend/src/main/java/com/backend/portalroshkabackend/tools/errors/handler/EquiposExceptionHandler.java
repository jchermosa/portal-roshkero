package com.backend.portalroshkabackend.tools.errors.handler;

import com.backend.portalroshkabackend.DTO.ErrorResponseDto;
import com.backend.portalroshkabackend.tools.errors.errorslist.EquipoNotFoundException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(1)
public class EquiposExceptionHandler extends BaseExceptionHandler {

    @ExceptionHandler(EquipoNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleEquipoNotFound(EquipoNotFoundException ex){
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage());
    }
}
