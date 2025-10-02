package com.backend.portalroshkabackend.Services.HumanResource.subservices;

import com.backend.portalroshkabackend.Models.Solicitud;
import com.backend.portalroshkabackend.Models.Usuario;
import com.backend.portalroshkabackend.Models.VacacionesAsignadas;
import com.backend.portalroshkabackend.Repositories.TH.UserRepository;
import com.backend.portalroshkabackend.Repositories.TH.VacacionesAsignadasRepository;
import com.backend.portalroshkabackend.tools.RepositoryService;
import com.backend.portalroshkabackend.tools.errors.errorslist.solicitudes.RequestAlreadyAcceptedException;
import com.backend.portalroshkabackend.tools.errors.errorslist.solicitudes.RequestNotFoundException;
import com.backend.portalroshkabackend.tools.errors.errorslist.user.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.backend.portalroshkabackend.tools.MessagesConst.DATABASE_DEFAULT_ERROR;

@Service("acceptVacationsService")
public class VacationsServiceImpl implements IAcceptRequestService {
    private final UserRepository userRepository;
    private final VacacionesAsignadasRepository vacacionesAsignadasRepository;
    private final RepositoryService repositoryService;

    @Autowired
    public VacationsServiceImpl(VacacionesAsignadasRepository vacacionesAsignadasRepository,
                                UserRepository userRepository,
                                RepositoryService repositoryService
                                ){
        this.userRepository = userRepository;
        this.vacacionesAsignadasRepository = vacacionesAsignadasRepository;
        this.repositoryService = repositoryService;
    }

    @Transactional
    @Override
    public void acceptRequest(Solicitud request) {
        Optional<VacacionesAsignadas> vacacionesAsignadasOptional = vacacionesAsignadasRepository.findBySolicitud_idSolicitud(request.getIdSolicitud());

        VacacionesAsignadas vacacionesAsignadas;

        if (vacacionesAsignadasOptional.isPresent()){
            vacacionesAsignadas = vacacionesAsignadasOptional.get();

            if (vacacionesAsignadas.getConfirmacionTH() == true) throw new RequestAlreadyAcceptedException(request.getIdSolicitud());

            vacacionesAsignadas.setConfirmacionTH(true);

        } else {
            vacacionesAsignadas = new VacacionesAsignadas();

            vacacionesAsignadas.setSolicitud(request);
            vacacionesAsignadas.setDiasUtilizados(request.getCantDias());
            vacacionesAsignadas.setFechaCreacion(LocalDateTime.now());
            vacacionesAsignadas.setConfirmacionTH(true);

            Usuario user = request.getUsuario();

            user.setDiasVacacionesRestante(user.getDiasVacacionesRestante() - request.getCantDias());

            repositoryService.save(
                    userRepository,
                    user,
                    DATABASE_DEFAULT_ERROR
            );

        }


        repositoryService.save(
                vacacionesAsignadasRepository,
                vacacionesAsignadas,
                DATABASE_DEFAULT_ERROR
        );


    }
}
