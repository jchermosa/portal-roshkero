package com.backend.portalroshkabackend.tools.validator.employee.update;

import com.backend.portalroshkabackend.DTO.common.UserUpdateDto;
import com.backend.portalroshkabackend.tools.validator.ValidatorStrategy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("employeeUpdateValidator")
public class EmployeeUpdateValidatorComposite implements ValidatorStrategy<UserUpdateDto> {
    private final List<ValidatorStrategy<UserUpdateDto>> validators;

    public EmployeeUpdateValidatorComposite(List<ValidatorStrategy<UserUpdateDto>> validators){
        this.validators = validators;
    }
    @Override
    public void validate(UserUpdateDto dto) {
        for (ValidatorStrategy<UserUpdateDto> validator : validators){
            validator.validate(dto);
        }
    }
}
