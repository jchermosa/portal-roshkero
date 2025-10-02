package com.backend.portalroshkabackend.tools.validator.employee.insert;

import com.backend.portalroshkabackend.DTO.common.UserInsertDto;
import com.backend.portalroshkabackend.tools.validator.ValidatorStrategy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("employeeInsertValidator")
public class EmployeeInsertValidatorComposite implements ValidatorStrategy<UserInsertDto> {
    private final List<ValidatorStrategy<UserInsertDto>> validators;

    public EmployeeInsertValidatorComposite(List<ValidatorStrategy<UserInsertDto>> validators){
        this.validators = validators;
    }

    @Override
    public void validate(UserInsertDto dto) {
        for (ValidatorStrategy<UserInsertDto> validator : validators){
            validator.validate(dto);
        }
    }
}
