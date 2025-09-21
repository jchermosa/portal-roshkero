package com.backend.portalroshkabackend.tools.errors.handler;

import com.backend.portalroshkabackend.DTO.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class BaseExceptionHandler {
    protected ResponseEntity<ErrorResponseDto> buildError(HttpStatus status, String message){
        ErrorResponseDto errorDto = new ErrorResponseDto();

        errorDto.setStatus(status.value());
        errorDto.setMessage(message);

        return new ResponseEntity<>(errorDto, status);
    }
}
