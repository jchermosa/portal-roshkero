package com.backend.portalroshkabackend.Controllers.HumanResource;

import com.backend.portalroshkabackend.DTO.EmailUpdatedDto;
import com.backend.portalroshkabackend.DTO.PhoneUpdatedDto;
import com.backend.portalroshkabackend.DTO.th.self.*;
import com.backend.portalroshkabackend.Services.HumanResource.IThSelfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
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

    @GetMapping("/th/me/{id}/requests") // "id" se refiere a la ID DEL USUARIO
    public ResponseEntity<Page<MisSolicitudesResponseDto>> getSelfRequests(
            @PathVariable int id,
            @PageableDefault(size = 10, direction = Sort.Direction.ASC) Pageable pageable
    ){
        Page<MisSolicitudesResponseDto> selfRequestsDto = selfService.getAllSelfRequests(id, pageable);

        return ResponseEntity.ok(selfRequestsDto);
    }

    @GetMapping("/th/me/{idUsuario}/requests/{idSolicitudTh}") // la segunda "id" se refiere al ID DE LA SOLICITUD
    public ResponseEntity<SolicitudEspecificaResponseDto> getSelfRequestById(
            @PathVariable int idUsuario,
            @PathVariable int idSolicitudTh
    ){
        SolicitudEspecificaResponseDto solicitudEspecificaResponseDto = selfService.getRequestById(idUsuario, idSolicitudTh);

        return ResponseEntity.ok(solicitudEspecificaResponseDto);
    }

    @PostMapping("/th/me/request/{idUsuario}")
    public ResponseEntity<RequestResponseDto> sendRequest(@PathVariable int idUsuario,
                                                          @RequestBody SendSolicitudDto sendSolicitudDto
    ){

        RequestResponseDto response = selfService.sendRequest(idUsuario, sendSolicitudDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("th/me/requests/{id}")
                .buildAndExpand(response.getId())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/th/me/requests/{idSolicitudTh}/update")
    public ResponseEntity<RequestResponseDto> updateRequest(@PathVariable int idSolicitudTh,
                                                            @RequestBody UpdateSolicitudDto updateSolicitudDto
    ){
        RequestResponseDto responseDto = selfService.updateRequest(idSolicitudTh, updateSolicitudDto);

        return ResponseEntity.ok(responseDto);
    }
}
