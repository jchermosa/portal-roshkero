package com.backend.portalroshkabackend.tools.validator.employee.insert;

import com.backend.portalroshkabackend.DTO.common.UserInsertDto;
import com.backend.portalroshkabackend.Repositories.TH.UserRepository;
import com.backend.portalroshkabackend.tools.errors.errorslist.user.DuplicateTelefonoException;
import com.backend.portalroshkabackend.tools.validator.ValidatorStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UniquePhoneValidator implements ValidatorStrategy<UserInsertDto> {
    private final UserRepository userRepository;

    @Autowired
    public UniquePhoneValidator(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public void validate(UserInsertDto target) {
        boolean exists = userRepository.existsByTelefono(target.getTelefono());

        if (exists) throw new DuplicateTelefonoException(target.getTelefono());
    }
}
