package com.backend.portalroshkabackend.Services.HumanResource;

import com.backend.portalroshkabackend.DTO.EmailUpdatedDto;
import com.backend.portalroshkabackend.DTO.PhoneUpdatedDto;

public interface ITHService {
    EmailUpdatedDto updateEmail(int id, String newEmail);
    PhoneUpdatedDto updatePhone(int id, String newPhone);
}
