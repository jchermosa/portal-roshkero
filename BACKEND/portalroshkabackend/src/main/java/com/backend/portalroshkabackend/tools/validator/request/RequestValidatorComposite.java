package com.backend.portalroshkabackend.tools.validator.request;

import com.backend.portalroshkabackend.DTO.common.UserUpdateDto;
import com.backend.portalroshkabackend.Models.Solicitud;
import com.backend.portalroshkabackend.tools.validator.ValidatorStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("requestHandlerValidator")
public class RequestValidatorComposite implements ValidatorStrategy<Solicitud> {
    private final List<ValidatorStrategy<Solicitud>> validators;

    @Autowired
    public RequestValidatorComposite(List<ValidatorStrategy<Solicitud>> validators){
        this.validators = validators;
    }
    @Override
    public void validate(Solicitud solicitud) {
        for (ValidatorStrategy<Solicitud> validator : validators){
            validator.validate(solicitud);
        }
    }
}
