package com.backend.portalroshkabackend.Controllers.HumanResource;

import com.backend.portalroshkabackend.DTO.EmailUpdatedDto;
import com.backend.portalroshkabackend.DTO.PhoneUpdatedDto;
import com.backend.portalroshkabackend.DTO.th.*;
import com.backend.portalroshkabackend.Services.HumanResource.IThSelfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
public class SelfController {
    private final IThSelfService selfService;

    @Autowired
    public SelfController(IThSelfService selfService) {
        this.selfService = selfService;
    }

    @PutMapping("/th/me/update/{id}")
    public ResponseEntity<?> updateMe(
            @RequestParam(value = "newEmail", required = false) String newEmail,
            @RequestParam(value = "newPhone", required = false) String newPhone,
            @PathVariable int id
    ) {
        if (newEmail != null) {
            EmailUpdatedDto dto = selfService.updateEmail(id, newEmail);
            return ResponseEntity.ok(dto);
        } else if (newPhone != null) {
            PhoneUpdatedDto dto = selfService.updatePhone(id, newPhone);
            return ResponseEntity.ok(dto);
        }

        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/th/me/benefits/types")
    public ResponseEntity<List<BenefitsTypesResponseDto>> getAllBenefitsTypes() {
        List<BenefitsTypesResponseDto> benefitsTypesDto = selfService.getAllBenefitsTypes();

        return ResponseEntity.ok(benefitsTypesDto);
    }

    @GetMapping("/th/me/devices/types")
    public ResponseEntity<List<DevicesTypesResponseDto>> getAllDevicesTypes() {
        List<DevicesTypesResponseDto> devicesTypesDto = selfService.getAllDevicesTypes();

        return ResponseEntity.ok(devicesTypesDto);
    }

    @GetMapping("/th/me/permission/types")
    public ResponseEntity<List<PermissionsTypesResponseDto>> getAllPermissionsTypes() {
        List<PermissionsTypesResponseDto> permissionsTypesDto = selfService.getAllPermissionsTypes();

        return ResponseEntity.ok(permissionsTypesDto);
    }

    @GetMapping("/th/me/request/types")
    public ResponseEntity<List<SolicitudTHTipoResponseDto>> getAllRequestTypes() {
        List<SolicitudTHTipoResponseDto> requestsTypesDto = selfService.getAllRequestTypes();

        return ResponseEntity.ok(requestsTypesDto);
    }

    @PostMapping("/th/me/request/{id}")
    public ResponseEntity<RequestResponseDto> sendRequest(@PathVariable int id,
                                         @RequestBody SendSolicitudDto sendSolicitudDto
    ){

        RequestResponseDto response = selfService.sendRequest(id, sendSolicitudDto);

        return ResponseEntity.ok(response);
    }
}
