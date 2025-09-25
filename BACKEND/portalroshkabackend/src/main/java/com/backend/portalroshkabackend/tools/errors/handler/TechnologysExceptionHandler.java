package com.backend.portalroshkabackend.tools.errors.handler;

import com.backend.portalroshkabackend.DTO.common.ErrorResponseDto;
import com.backend.portalroshkabackend.tools.errors.errorslist.tecnologias.*;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(1)
public class TechnologysExceptionHandler extends BaseExceptionHandler{

    @ExceptionHandler(TechnologyInUseException.class)
    public ResponseEntity<ErrorResponseDto> handleTechnologyInUse(TechnologyInUseException ex){
        return buildError(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(DuplicateUserTechnologyException.class)
    public ResponseEntity<ErrorResponseDto> handleDuplicateUserTechnology(DuplicateUserTechnologyException ex){
        return buildError(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(DuplicateTeamTechnologyException.class)
    public ResponseEntity<ErrorResponseDto> handleDuplicateTeamTechnology(DuplicateTeamTechnologyException ex){
        return buildError(HttpStatus.CONFLICT, ex.getMessage());
    }
}
