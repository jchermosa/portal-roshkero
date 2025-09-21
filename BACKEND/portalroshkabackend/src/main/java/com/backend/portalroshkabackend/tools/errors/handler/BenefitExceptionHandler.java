package com.backend.portalroshkabackend.tools.errors.handler;

import com.backend.portalroshkabackend.DTO.ErrorResponseDto;
import com.backend.portalroshkabackend.tools.errors.errorslist.beneficios.BenefitTypeInUseException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Order(1)
public class BenefitExceptionHandler extends BaseExceptionHandler{

    @ExceptionHandler(BenefitTypeInUseException.class)
    public ResponseEntity<ErrorResponseDto> handlerBenefitTypeInUse(BenefitTypeInUseException ex){
        return buildError(HttpStatus.CONFLICT, ex.getMessage());
    }

}
