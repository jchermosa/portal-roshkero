package com.backend.portalroshkabackend.tools.errors.handler;

import com.backend.portalroshkabackend.DTO.ErrorResponseDto;
import com.backend.portalroshkabackend.tools.errors.errorslist.solicitudes.RequestAlreadyAcceptedException;
import com.backend.portalroshkabackend.tools.errors.errorslist.solicitudes.RequestAlreadyRejectedException;
import com.backend.portalroshkabackend.tools.errors.errorslist.solicitudes.RequestNotFoundException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(1)
public class RequestsExceptionHandler extends BaseExceptionHandler{

    @ExceptionHandler(RequestNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleRequestNotFound(RequestNotFoundException ex){
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(RequestAlreadyRejectedException.class)
    public ResponseEntity<ErrorResponseDto> handleRequestAlreadyRejected(RequestAlreadyRejectedException ex){
        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(RequestAlreadyAcceptedException.class)
    public ResponseEntity<ErrorResponseDto> handleRequestAlreadyAccepted(RequestAlreadyAcceptedException ex){
        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage());
    }
}
