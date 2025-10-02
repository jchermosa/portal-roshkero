package com.backend.portalroshkabackend.tools.validator.employee.delete;

import com.backend.portalroshkabackend.Models.Enum.EstadoActivoInactivo;
import com.backend.portalroshkabackend.Models.Usuario;
import com.backend.portalroshkabackend.tools.errors.errorslist.user.UserAlreadyInactiveException;
import com.backend.portalroshkabackend.tools.validator.ValidatorStrategy;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMustBeActiveValidator implements ValidatorStrategy<Usuario> {

    @Override
    public void validate(Usuario user) {
        if (user.getEstado() == EstadoActivoInactivo.I) throw new UserAlreadyInactiveException(user.getNombre(), user.getApellido());
    }
}
