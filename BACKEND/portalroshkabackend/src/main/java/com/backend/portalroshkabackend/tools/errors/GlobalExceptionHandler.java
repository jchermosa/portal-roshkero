package com.backend.portalroshkabackend.tools.errors;


import com.backend.portalroshkabackend.DTO.ErrorResponseDto;
import com.backend.portalroshkabackend.tools.errors.errorslist.*;
import com.sun.net.httpserver.HttpsServer;
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
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(UserAlreadyInactiveException.class)
    public ResponseEntity<ErrorResponseDto> handleUserAlreadyInactive(UserAlreadyInactiveException ex){
        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    // --- SOLICITUDES EXCEPTIONS HANDLER ---

    @ExceptionHandler(RequestNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleRequestNotFound(RequestNotFoundException ex){
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // --- CARGOS EXCEPTIONS HANDLER ---

    @ExceptionHandler(CargoNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handlePositionNotFound(CargoNotFoundException ex){
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(CargoAlreadyInactiveException.class)
    public ResponseEntity<ErrorResponseDto> handlePositionAlreadyInactive(CargoAlreadyInactiveException ex){
        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    // --- ROLES EXCEPTIONS HANDLER ---
    @ExceptionHandler(RolesNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleRolesNotFound(RolesNotFoundException ex){
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // --- EQUIPOS EXCEPTIONS HANDLER ---
    @ExceptionHandler(EquipoNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleEquipoNotFound(EquipoNotFoundException ex){
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // --- DATA BASE ---
    @ExceptionHandler(DatabaseOperationException.class)
    public ResponseEntity<ErrorResponseDto> handleDatabaseOperation(DatabaseOperationException ex){
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    // --- DUPLICATIONS EXCEPTION HANDLER ---

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ErrorResponseDto> handleDuplicateEmail(DuplicateEmailException ex){
        return buildError(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(DuplicateCedulaException.class)
    public ResponseEntity<ErrorResponseDto> handleDuplicateCedula(DuplicateCedulaException ex){
        return buildError(HttpStatus.CONFLICT, ex.getMessage());
    }

    // --- GENERAL EXCEPTION HANDLER ---

    @ExceptionHandler
    public ResponseEntity<ErrorResponseDto> handleGeneralError(Exception ex){
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrio un error inesperado" + ex.getMessage());
    }



    private ResponseEntity<ErrorResponseDto> buildError(HttpStatus status, String message){
        ErrorResponseDto errorDto = new ErrorResponseDto();

        errorDto.setStatus(status.value());
        errorDto.setMessage(message);

        return new ResponseEntity<>(errorDto, status);
    }
}
