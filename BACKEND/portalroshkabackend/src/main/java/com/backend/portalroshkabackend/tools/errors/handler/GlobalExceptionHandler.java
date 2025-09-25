package com.backend.portalroshkabackend.tools.errors.handler;


import com.backend.portalroshkabackend.DTO.common.ErrorResponseDto;
import com.backend.portalroshkabackend.tools.errors.errorslist.*;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(2)
public class GlobalExceptionHandler extends BaseExceptionHandler{

    // --- GENERAL HANDLER ---
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGeneralError(Exception ex){
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrio un error inesperado: " + ex.getMessage());
    }

    // --- DATA BASE ---
    @ExceptionHandler(DatabaseOperationException.class)
    public ResponseEntity<ErrorResponseDto> handleDatabaseOperation(DatabaseOperationException ex){
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    // --- INVALID ARGUMENT ---
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDto> handleIllegalArgument(IllegalArgumentException ex){
        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage());
    }









}
