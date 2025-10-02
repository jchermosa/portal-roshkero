package com.backend.portalroshkabackend.tools.errors.handler;

import com.backend.portalroshkabackend.DTO.common.ErrorResponseDto;
import com.backend.portalroshkabackend.tools.errors.errorslist.asignacionDispositivos.DeviceNotAvailableException;
import com.backend.portalroshkabackend.tools.errors.errorslist.dispositivos.*;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(1)
public class DeviceExceptionHandler extends BaseExceptionHandler {
    @ExceptionHandler(DeviceNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleDeviceNotFoundException(DeviceNotFoundException ex) {
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(UniqueSerialException.class)
    public ResponseEntity<ErrorResponseDto> handleUniqueSerialException(UniqueSerialException ex) {
        return buildError(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(InvalidFilterValueException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidFilterValueException(InvalidFilterValueException ex) {
        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(LocationNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleLocationNotFoundException(LocationNotFoundException ex) {
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(DeviceMappingException.class)
    public  ResponseEntity<ErrorResponseDto> handleDeviceMappingException(DeviceMappingException ex) {
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, "Error al procesar los datos de los dispositivos");
    }
}
