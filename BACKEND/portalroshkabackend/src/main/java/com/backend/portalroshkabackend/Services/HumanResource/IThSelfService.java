package com.backend.portalroshkabackend.Services.HumanResource;

import com.backend.portalroshkabackend.DTO.EmailUpdatedDto;
import com.backend.portalroshkabackend.DTO.PhoneUpdatedDto;
import com.backend.portalroshkabackend.DTO.th.*;

import java.util.List;

public interface IThSelfService {
    EmailUpdatedDto updateEmail(int id, String newEmail);
    PhoneUpdatedDto updatePhone(int id, String newPhone);

    RequestResponseDto sendRequest(int id, SendSolicitudDto sendSolicitudDto);

    List<BenefitsTypesResponseDto> getAllBenefitsTypes();
    List<DevicesTypesResponseDto> getAllDevicesTypes();
    List<PermissionsTypesResponseDto> getAllPermissionsTypes();
    List<SolicitudTHTipoResponseDto> getAllRequestTypes();
}
