package com.backend.portalroshkabackend.tools.errors;


import com.backend.portalroshkabackend.DTO.ErrorResponseDto;
import com.backend.portalroshkabackend.tools.errors.errorslist.*;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // --- USER EXCEPTION HANDLE ---

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleUserNotFound(UserNotFoundException ex){
        ErrorResponseDto errorDto = new ErrorResponseDto();

        errorDto.setStatus(HttpStatus.NOT_FOUND.value());
        errorDto.setMessage(ex.getMessage());

        return new ResponseEntity<>(errorDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserAlreadyInactiveException.class)
    public ResponseEntity<ErrorResponseDto> handleUserAlreadyInactive(UserAlreadyInactiveException ex){
        ErrorResponseDto errorDto = new ErrorResponseDto();

        errorDto.setStatus(HttpStatus.BAD_REQUEST.value());
        errorDto.setMessage(ex.getMessage());

        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    // --- SOLICITUDES EXCEPTIONS HANDLER ---

    @ExceptionHandler(RequestNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleRequestNotFound(RequestNotFoundException ex){
        ErrorResponseDto errorDto = new ErrorResponseDto();

        errorDto.setStatus(HttpStatus.BAD_REQUEST.value());
        errorDto.setMessage(ex.getMessage());

        return new ResponseEntity<>(errorDto, HttpStatus.NOT_FOUND);
    }

    // --- CARGOS EXCEPTIONS HANDLER ---

    @ExceptionHandler(CargoNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handlePositionNotFound(CargoNotFoundException ex){
        ErrorResponseDto errorDto = new ErrorResponseDto();

        errorDto.setStatus(HttpStatus.NOT_FOUND.value());
        errorDto.setMessage(ex.getMessage());

        return new ResponseEntity<>(errorDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CargoAlreadyInactiveException.class)
    public ResponseEntity<ErrorResponseDto> handlePositionAlreadyInactive(CargoAlreadyInactiveException ex){
        ErrorResponseDto errorDto = new ErrorResponseDto();

        errorDto.setStatus(HttpStatus.BAD_REQUEST.value());
        errorDto.setMessage(ex.getMessage());

        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    // --- GENERAL EXCEPTION HANDLER

    @ExceptionHandler
    public ResponseEntity<ErrorResponseDto> handleGeneralError(Exception ex){
        ErrorResponseDto errorDto = new ErrorResponseDto();

        errorDto.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorDto.setMessage("Ocurrio un error inesperado: " + ex.getMessage());

        return new ResponseEntity<>(errorDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
