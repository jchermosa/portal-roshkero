package com.backend.portalroshkabackend.tools.validator.employee.insert;

import com.backend.portalroshkabackend.DTO.common.UserInsertDto;
import com.backend.portalroshkabackend.Repositories.TH.UserRepository;
import com.backend.portalroshkabackend.tools.errors.errorslist.user.DuplicateEmailException;
import com.backend.portalroshkabackend.tools.validator.ValidatorStrategy;
import org.springframework.stereotype.Component;

@Component
public class UniqueEmailValidator implements ValidatorStrategy<UserInsertDto> {
    private final UserRepository userRepository;

    public UniqueEmailValidator(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public void validate(UserInsertDto dto) {
        boolean exists = userRepository.existsByCorreo(dto.getCorreo());

        if (exists) throw new DuplicateEmailException(dto.getCorreo());
    }
}
