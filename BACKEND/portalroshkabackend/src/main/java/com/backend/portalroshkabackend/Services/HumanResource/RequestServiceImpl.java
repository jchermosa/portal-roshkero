package com.backend.portalroshkabackend.Services.HumanResource;

import com.backend.portalroshkabackend.Models.Enum.EstadoSolicitudEnum;
import com.backend.portalroshkabackend.DTO.th.*;
import com.backend.portalroshkabackend.DTO.th.request.RequestResponseDto;
import com.backend.portalroshkabackend.Models.Enum.SolicitudesEnum;
import com.backend.portalroshkabackend.Models.Solicitud;
import com.backend.portalroshkabackend.Repositories.TH.*;
import com.backend.portalroshkabackend.Repositories.TH.SolicitudRepository;
import com.backend.portalroshkabackend.Repositories.*;
import com.backend.portalroshkabackend.Services.HumanResource.subservices.IAcceptRequestService;
import com.backend.portalroshkabackend.tools.RepositoryService;
import com.backend.portalroshkabackend.tools.errors.errorslist.solicitudes.RequestNotFoundException;
import com.backend.portalroshkabackend.tools.mapper.RequestMapper;
import com.backend.portalroshkabackend.tools.validator.RequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.backend.portalroshkabackend.tools.MessagesConst.*;

@Service("humanRequestService")
public class RequestServiceImpl implements IRequestService{
    private final SolicitudRepository solicitudRepository;
    private final RequestValidator requestValidator;
    private final RepositoryService repositoryService;
    private final IAcceptRequestService acceptVacationsService;
    private final IAcceptRequestService acceptBenefitService;
    private final IAcceptRequestService acceptPermissionsService;

    @Autowired
    public RequestServiceImpl (SolicitudRepository solicitudRepository,
                               RequestValidator requestValidator,
                               RepositoryService repositoryService,
                               @Qualifier("acceptVacationsService") IAcceptRequestService acceptVacationsService,
                               @Qualifier("acceptBenefitService") IAcceptRequestService acceptBenefitService,
                               @Qualifier("acceptPermissionsService") IAcceptRequestService acceptPermissionsService
    ){
        this.solicitudRepository = solicitudRepository;
        this.requestValidator = requestValidator;
        this.repositoryService = repositoryService;
        this.acceptVacationsService = acceptVacationsService;
        this.acceptBenefitService = acceptBenefitService;
        this.acceptPermissionsService = acceptPermissionsService;
    }

    @Transactional(readOnly = true)
    @Override
    public Page<SolicitudResponseDto> getByTipoSolicitud(SolicitudesEnum tipoSolicitud, Pageable pageable) {
        Page<Solicitud> requestSorted = solicitudRepository.findAllByTipoSolicitud(tipoSolicitud, pageable);

        return requestSorted.map(RequestMapper::toSolicitudTHResponseDto);
    }

    @Transactional
    @Override
    public RequestResponseDto acceptRequest(int idRequest) {
        Solicitud request = repositoryService.findByIdOrThrow(
                solicitudRepository,
                idRequest,
                () -> new RequestNotFoundException(idRequest)
        );

        requestValidator.validateRequestStatus(request.getEstado(), request.getIdSolicitud());

        switch (request.getTipoSolicitud()){
            case SolicitudesEnum.VACACIONES -> acceptVacationsService.acceptRequest(request);
            case SolicitudesEnum.BENEFICIO -> acceptBenefitService.acceptRequest(request);
            case SolicitudesEnum.PERMISO -> acceptPermissionsService.acceptRequest(request);
        }

        request.setEstado(EstadoSolicitudEnum.A);

        Solicitud acceptedRequest = repositoryService.save(
                solicitudRepository,
                request,
                DATABASE_DEFAULT_ERROR
        );

        return RequestMapper.toRequestResponseDto(acceptedRequest.getIdSolicitud(), REQUEST_ACCEPTED_MESSAGE);
    }

    @Transactional
    @Override
    public RequestResponseDto rejectRequest(int idRequest) {
        Solicitud request = repositoryService.findByIdOrThrow(
                solicitudRepository,
                idRequest,
                () -> new RequestNotFoundException(idRequest)
        );

        requestValidator.validateRequestStatus(request.getEstado(), request.getIdSolicitud());

        request.setEstado(EstadoSolicitudEnum.R); // Setea la solicitud como rechazada

        Solicitud rejectedRequest =  repositoryService.save(
                solicitudRepository,
                request,
                DATABASE_DEFAULT_ERROR
        );

        return RequestMapper.toRequestResponseDto(rejectedRequest.getIdSolicitud(), REQUEST_REJECTED_MESSAGE);

    }

}
