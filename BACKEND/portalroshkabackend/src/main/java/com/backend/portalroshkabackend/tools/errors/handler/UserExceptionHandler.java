package com.backend.portalroshkabackend.tools.errors.handler;

import com.backend.portalroshkabackend.DTO.ErrorResponseDto;
import com.backend.portalroshkabackend.tools.errors.errorslist.user.*;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(1)
public class UserExceptionHandler extends BaseExceptionHandler{

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleUserNotFound(UserNotFoundException ex){
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(UserAlreadyInactiveException.class)
    public ResponseEntity<ErrorResponseDto> handleUserAlreadyInactive(UserAlreadyInactiveException ex){
        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(UserHavePendientRequestsException.class)
    public ResponseEntity<ErrorResponseDto> handleUserHavePendientRequestException(UserHavePendientRequestsException ex){
        return buildError(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ErrorResponseDto> handleDuplicateEmail(DuplicateEmailException ex){
        return buildError(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(DuplicateCedulaException.class)
    public ResponseEntity<ErrorResponseDto> handleDuplicateCedula(DuplicateCedulaException ex){
        return buildError(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(DuplicateTelefonoException.class)
    public ResponseEntity<ErrorResponseDto> handleDuplicateTelefono(DuplicateTelefonoException ex){
        return buildError(HttpStatus.CONFLICT, ex.getMessage());
    }
}
