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

import static com.backend.portalroshkabackend.tools.MessagesConst.DATABASE_DEFAULT_ERROR;

@Service("acceptVacationsService")
public class VacationsServiceImpl implements IAcceptRequestService {
    private final VacacionesAsignadasRepository vacacionesAsignadasRepository;
    private final RepositoryService repositoryService;

    @Autowired
    public VacationsServiceImpl(VacacionesAsignadasRepository vacacionesAsignadasRepository,
                                RepositoryService repositoryService
                                ){
        this.vacacionesAsignadasRepository = vacacionesAsignadasRepository;
        this.repositoryService = repositoryService;
    }

    @Transactional
    @Override
    public void acceptRequest(Solicitud request) {
        VacacionesAsignadas vacacionesAsignadas = vacacionesAsignadasRepository.findBySolicitud_idSolicitud(request.getIdSolicitud()).orElseThrow(() -> new RequestNotFoundException(request.getIdSolicitud()));

        if (vacacionesAsignadas.getConfirmacionTH() == true) throw new RequestAlreadyAcceptedException(request.getIdSolicitud());

        vacacionesAsignadas.setConfirmacionTH(true);

        repositoryService.save(
                vacacionesAsignadasRepository,
                vacacionesAsignadas,
                DATABASE_DEFAULT_ERROR
        );

    }
}
