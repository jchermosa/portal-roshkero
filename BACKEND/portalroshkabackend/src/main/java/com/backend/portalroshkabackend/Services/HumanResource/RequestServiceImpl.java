package com.backend.portalroshkabackend.Services.HumanResource;

import com.backend.portalroshkabackend.Models.Enum.EstadoSolicitudEnum;
import com.backend.portalroshkabackend.DTO.th.*;
import com.backend.portalroshkabackend.DTO.th.request.RequestResponseDto;
import com.backend.portalroshkabackend.Models.Enum.SolicitudesEnum;
import com.backend.portalroshkabackend.Models.Solicitud;
import com.backend.portalroshkabackend.Repositories.TH.SolicitudRepository;
import com.backend.portalroshkabackend.Services.HumanResource.subservices.IAcceptRequestService;
import com.backend.portalroshkabackend.tools.RepositoryService;
import com.backend.portalroshkabackend.tools.errors.errorslist.solicitudes.RequestNotFoundException;
import com.backend.portalroshkabackend.tools.mapper.RequestMapper;
import com.backend.portalroshkabackend.tools.validator.ValidatorStrategy;
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
    private final RepositoryService repositoryService;
    private final IAcceptRequestService acceptVacationsService;
    private final IAcceptRequestService acceptBenefitService;
    private final IAcceptRequestService acceptPermissionsService;
    private final RequestMapper requestMapper;
    ValidatorStrategy<Solicitud> requestValidator;

    @Autowired
    public RequestServiceImpl (SolicitudRepository solicitudRepository,
                               RepositoryService repositoryService,
                               RequestMapper requestMapper,
                               @Qualifier("acceptVacationsService") IAcceptRequestService acceptVacationsService,
                               @Qualifier("acceptBenefitService") IAcceptRequestService acceptBenefitService,
                               @Qualifier("acceptPermissionsService") IAcceptRequestService acceptPermissionsService,
                               @Qualifier("requestHandlerValidator")ValidatorStrategy<Solicitud> requestValidator
    ){
        this.solicitudRepository = solicitudRepository;
        this.requestValidator = requestValidator;
        this.repositoryService = repositoryService;
        this.acceptVacationsService = acceptVacationsService;
        this.acceptBenefitService = acceptBenefitService;
        this.acceptPermissionsService = acceptPermissionsService;
        this.requestMapper = requestMapper;
    }

    @Transactional(readOnly = true)
    @Override
    public Page<SolicitudResponseDto> getBenefitsOrPermissions(SolicitudesEnum tipoSolicitud, Pageable pageable) {

        Page<Solicitud> requestSorted;

        switch (tipoSolicitud) {
            case PERMISO -> {
                requestSorted = solicitudRepository.findAllByTipoSolicitudAndEstadoOrTipoSolicitudAndLiderIsNull(SolicitudesEnum.PERMISO, EstadoSolicitudEnum.A, SolicitudesEnum.PERMISO, pageable);
                return requestSorted.map(requestMapper::toPermissionResponseDto);
            }
            case BENEFICIO -> {
                requestSorted = solicitudRepository.findAllByTipoSolicitud(tipoSolicitud, pageable);
                return requestSorted.map(requestMapper::toBenefitsResponseDto);
            }

            default -> throw new IllegalArgumentException("Tipo de solicitud no soportado: " + tipoSolicitud);
        }

    }

    @Override
    public Page<SolicitudResponseDto> getVacations(SolicitudesEnum tipoSolicitud, Pageable pageable) {
        Page<Solicitud> vacations = solicitudRepository.findAllByTipoSolicitudAndEstado(SolicitudesEnum.VACACIONES, EstadoSolicitudEnum.A, pageable);

        return vacations.map(requestMapper::toVacationsResponseDto);
    }

    @Override
    public SolicitudByIdResponseDto getRequestById(int idSolicitud) {
        Solicitud request = repositoryService.findByIdOrThrow(
                solicitudRepository,
                idSolicitud,
                () -> new RequestNotFoundException(idSolicitud)
        );

        return requestMapper.toSolicitudByIdResponseDto(request);
    }

    @Transactional
    @Override
    public RequestResponseDto acceptRequest(int idRequest) {
        Solicitud request = repositoryService.findByIdOrThrow(
                solicitudRepository,
                idRequest,
                () -> new RequestNotFoundException(idRequest)
        );

        if (request.getLider() == null) {
            requestValidator.validate(request);
            request.setEstado(EstadoSolicitudEnum.A);
        }

        switch (request.getTipoSolicitud()){
            case SolicitudesEnum.VACACIONES -> acceptVacationsService.acceptRequest(request);
            case SolicitudesEnum.BENEFICIO -> acceptBenefitService.acceptRequest(request);
            case SolicitudesEnum.PERMISO -> acceptPermissionsService.acceptRequest(request);
        }

        Solicitud acceptedRequest = repositoryService.save(
                solicitudRepository,
                request,
                DATABASE_DEFAULT_ERROR
        );

        return requestMapper.toRequestResponseDto(acceptedRequest.getIdSolicitud(), REQUEST_ACCEPTED_MESSAGE);
    }

    @Transactional
    @Override
    public RequestResponseDto rejectRequest(int idRequest) {
        Solicitud request = repositoryService.findByIdOrThrow(
                solicitudRepository,
                idRequest,
                () -> new RequestNotFoundException(idRequest)
        );

        requestValidator.validate(request);

        request.setEstado(EstadoSolicitudEnum.R); // Setea la solicitud como rechazada

        Solicitud rejectedRequest =  repositoryService.save(
                solicitudRepository,
                request,
                DATABASE_DEFAULT_ERROR
        );

        return requestMapper.toRequestResponseDto(rejectedRequest.getIdSolicitud(), REQUEST_REJECTED_MESSAGE);

    }

}
