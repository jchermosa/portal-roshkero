package com.backend.portalroshkabackend.tools.errors.handler;

import com.backend.portalroshkabackend.DTO.common.ErrorResponseDto;
import com.backend.portalroshkabackend.tools.errors.errorslist.asignacionDispositivos.*;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(1)
public class DeviceAssignmentExceptionHandler extends BaseExceptionHandler{
    @ExceptionHandler(DeviceNotAvailableException.class)
    public ResponseEntity<ErrorResponseDto> handleDeviceNotAvailableException(DeviceNotAvailableException ex) {
        return buildError(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(DeviceAssignmentNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleDeviceAssigmentNotFoundException(DeviceAssignmentNotFoundException ex) {
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(UsedDeviceRequest.class)
    public ResponseEntity<ErrorResponseDto> handleUsedDeviceRequest(UsedDeviceRequest ex){
        return buildError(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(InvalidRequestTypeException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidRequestTypeException(InvalidRequestTypeException ex){
        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage());
    }
}
