package com.backend.portalroshkabackend.tools.validator.employee.delete;

import com.backend.portalroshkabackend.Models.Usuario;
import com.backend.portalroshkabackend.tools.validator.ValidatorStrategy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("employeeDeleteValidator")
public class EmployeeDeleteValidatorComposite implements ValidatorStrategy<Usuario> {
    private final List<ValidatorStrategy<Usuario>> validators;

    public EmployeeDeleteValidatorComposite(List<ValidatorStrategy<Usuario>> validators){
        this.validators = validators;
    }

    @Override
    public void validate(Usuario user) {
        for (ValidatorStrategy<Usuario> validator : validators){
            validator.validate(user);
        }
    }
}
