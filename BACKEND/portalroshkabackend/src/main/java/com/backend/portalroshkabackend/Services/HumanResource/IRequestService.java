package com.backend.portalroshkabackend.Services.HumanResource;

import com.backend.portalroshkabackend.DTO.RequestDto;
import com.backend.portalroshkabackend.DTO.RequestRejectedDto;
import com.backend.portalroshkabackend.DTO.th.SolicitudTHResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IRequestService {
    Page<RequestDto> getAllRequests(Pageable pageable);
    Page<SolicitudTHResponseDto> getApprovedByLeader(Pageable pageable);
    boolean acceptRequest(int idRequest);
    boolean rejectRequest(int idRequest, RequestRejectedDto rejectedDto);
    RequestDto addNewRequestType();
}
