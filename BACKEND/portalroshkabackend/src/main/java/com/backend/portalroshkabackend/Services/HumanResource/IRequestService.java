package com.backend.portalroshkabackend.Services.HumanResource;

import com.backend.portalroshkabackend.DTO.RequestDto;
import com.backend.portalroshkabackend.DTO.RequestRejectedDto;
import com.backend.portalroshkabackend.DTO.th.BenefitsTypesResponseDto;
import com.backend.portalroshkabackend.DTO.th.DevicesTypesResponseDto;
import com.backend.portalroshkabackend.DTO.th.SolicitudTHResponseDto;
import com.backend.portalroshkabackend.DTO.th.SolicitudTHTipoResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IRequestService {
    Page<RequestDto> getAllRequests(Pageable pageable);
    Page<SolicitudTHResponseDto> getApprovedByLeader(Pageable pageable);

    List<BenefitsTypesResponseDto> getAllBenefitsTypes();
    List<DevicesTypesResponseDto> getAllDevicesTypes();
    List<SolicitudTHTipoResponseDto> getAllPermissionsTypes();

    boolean acceptRequest(int idRequest);
    boolean rejectRequest(int idRequest, RequestRejectedDto rejectedDto);
    RequestDto addNewRequestType();
}
