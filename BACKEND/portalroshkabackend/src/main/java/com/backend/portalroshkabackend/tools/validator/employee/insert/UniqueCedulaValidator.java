package com.backend.portalroshkabackend.tools.validator.employee.insert;

import com.backend.portalroshkabackend.DTO.common.UserInsertDto;
import com.backend.portalroshkabackend.Repositories.TH.UserRepository;
import com.backend.portalroshkabackend.tools.errors.errorslist.user.DuplicateCedulaException;
import com.backend.portalroshkabackend.tools.validator.ValidatorStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UniqueCedulaValidator implements ValidatorStrategy<UserInsertDto> {
    private final UserRepository userRepository;

    @Autowired
    public UniqueCedulaValidator(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public void validate(UserInsertDto dto) {
        boolean exists = userRepository.existsByNroCedula(dto.getNroCedula());

        if (exists) throw new DuplicateCedulaException(dto.getNroCedula());

    }
}
