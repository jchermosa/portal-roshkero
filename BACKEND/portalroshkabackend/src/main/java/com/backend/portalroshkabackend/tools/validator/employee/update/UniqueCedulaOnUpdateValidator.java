package com.backend.portalroshkabackend.tools.validator.employee.update;

import com.backend.portalroshkabackend.DTO.common.UserUpdateDto;
import com.backend.portalroshkabackend.Repositories.TH.UserRepository;
import com.backend.portalroshkabackend.tools.errors.errorslist.user.DuplicateCedulaException;
import com.backend.portalroshkabackend.tools.validator.ValidatorStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UniqueCedulaOnUpdateValidator implements ValidatorStrategy<UserUpdateDto> {
    private final UserRepository userRepository;

    @Autowired
    public UniqueCedulaOnUpdateValidator(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public void validate(UserUpdateDto dto) {
        boolean exists = userRepository.existsByNroCedulaAndIdUsuarioNot(dto.getNroCedula(), dto.getIdUsuario());

        if (exists) throw new DuplicateCedulaException(dto.getNroCedula());
    }
}
