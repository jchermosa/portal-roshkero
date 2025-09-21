package com.backend.portalroshkabackend.tools.errors.handler;


import com.backend.portalroshkabackend.DTO.ErrorResponseDto;
import com.backend.portalroshkabackend.tools.errors.errorslist.dispositivos.DeviceTypeInUseException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(1)
public class DispositivosExceptionHandler extends BaseExceptionHandler{

    @ExceptionHandler(DeviceTypeInUseException.class)
    public ResponseEntity<ErrorResponseDto> handlerDeviceTypeInUse(DeviceTypeInUseException ex){
        return buildError(HttpStatus.CONFLICT, ex.getMessage());
    }
}
