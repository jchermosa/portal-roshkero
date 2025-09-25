package com.backend.portalroshkabackend.tools.errors.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.backend.portalroshkabackend.DTO.common.ErrorResponseDto;

public class BaseExceptionHandler {
    protected ResponseEntity<ErrorResponseDto> buildError(HttpStatus status, String message){
        ErrorResponseDto errorDto = new ErrorResponseDto();

        errorDto.setStatus(status.value());
        errorDto.setMessage(message);

        return new ResponseEntity<>(errorDto, status);
    }
}
