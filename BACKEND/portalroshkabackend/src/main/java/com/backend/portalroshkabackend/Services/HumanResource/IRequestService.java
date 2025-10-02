package com.backend.portalroshkabackend.Services.HumanResource;

<<<<<<< HEAD
import com.backend.portalroshkabackend.DTO.RequestDto;
import com.backend.portalroshkabackend.DTO.RequestRejectedDto;
import com.backend.portalroshkabackend.DTO.th.*;
import com.backend.portalroshkabackend.Models.Enum.EstadoSolicitudEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IRequestService {
    Page<RequestDto> getAllRequests(Pageable pageable);
    Page<SolicitudTHResponseDto> getApprovedByLeader(Pageable pageable);

    Page<SolicitudTHResponseDto> getByEstado(EstadoSolicitudEnum estado, Pageable pageable);

    boolean acceptRequest(int idRequest);
    boolean rejectRequest(int idRequest, RequestRejectedDto rejectedDto);
    RequestDto addNewRequestType();
=======
import com.backend.portalroshkabackend.DTO.th.*;
import com.backend.portalroshkabackend.DTO.th.request.RequestResponseDto;
import com.backend.portalroshkabackend.Models.Enum.EstadoSolicitudEnum;
import com.backend.portalroshkabackend.Models.Enum.SolicitudesEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IRequestService {
    // Page<SolicitudTHResponseDto> getApprovedByLeader(Pageable pageable);

    Page<SolicitudResponseDto> getBenefitsOrPermissions(SolicitudesEnum tipoSolicitud, Pageable pageable);
    Page<SolicitudResponseDto> getVacations(SolicitudesEnum tipoSolicitud, Pageable pageable);
    SolicitudByIdResponseDto getRequestById(int idSolicitud);
    RequestResponseDto acceptRequest(int idRequest);
    RequestResponseDto rejectRequest(int idRequest);
    // RequestDto addNewRequestType();
>>>>>>> parent of dca61a3 (se elimino backend)
}
