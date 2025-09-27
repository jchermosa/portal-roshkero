package com.backend.portalroshkabackend.tools.validator.employee.delete;

import com.backend.portalroshkabackend.Models.Enum.EstadoSolicitudEnum;
import com.backend.portalroshkabackend.Models.Usuario;
import com.backend.portalroshkabackend.Repositories.TH.SolicitudRepository;
import com.backend.portalroshkabackend.tools.errors.errorslist.user.UserHavePendientRequestsException;
import com.backend.portalroshkabackend.tools.validator.ValidatorStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmployeeHasNoPendingRequestValidator implements ValidatorStrategy<Usuario> {
    private final SolicitudRepository solicitudRepository;

    @Autowired
    public EmployeeHasNoPendingRequestValidator(SolicitudRepository solicitudRepository){
        this.solicitudRepository = solicitudRepository;
    }

    @Override
    public void validate(Usuario user) {
        boolean hasPending= solicitudRepository.existsByUsuario_idUsuarioAndEstado(user.getIdUsuario(), EstadoSolicitudEnum.P);

        if (hasPending) throw new UserHavePendientRequestsException(user.getNombre(), user.getApellido());
    }
}
