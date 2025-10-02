package com.backend.portalroshkabackend.Controllers.SYSADMIN;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.portalroshkabackend.DTO.SYSADMIN.DeviceRequestDto;
import com.backend.portalroshkabackend.DTO.SYSADMIN.RequestDTO;
import com.backend.portalroshkabackend.Services.SysAdmin.DeviceRequest;
import com.backend.portalroshkabackend.Services.SysAdmin.SysAdminService;

@RestController
@RequestMapping("/api/v1/admin/sysadmin")
public class RequestDeviceController {


    @Autowired
    private final SysAdminService sysAdminService;

    @Autowired
    private final DeviceRequest deviceRequest;

    RequestDeviceController(SysAdminService sysAdminService, DeviceRequest deviceRequest) {
        this.sysAdminService = sysAdminService;
        this.deviceRequest = deviceRequest;
    }


    // ======> SOLICITUDES DE DISPOSITIVOS <========

    //Obtener todas las solicitudes   
   @GetMapping("/allRequests")
   public Page<RequestDTO> getAllRequests(Pageable pageable) {
       return sysAdminService.findAllSolicitudes(pageable);
   }

//    Create a controller to obtain a single request by its ID
    @GetMapping("/request/{idRequest}")
    public ResponseEntity<?> getRequestById(@PathVariable Integer idRequest) {
        DeviceRequestDto requestDto = deviceRequest.getRequestById(idRequest);
        if (requestDto == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Solicitud no encontrada");
        }
        return ResponseEntity.ok(requestDto);
    }

    //Aceptar
    @PostMapping("deviceRequest/{idRequest}/accept")
    public ResponseEntity<?> acceptRequest(@PathVariable Integer idRequest) {

        DeviceRequestDto updatedRequest = deviceRequest.acceptRequest(idRequest);
        if (updatedRequest == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Solicitud no encontrada");
        }

        return ResponseEntity.ok(updatedRequest);
    }


    // Rechazar
    @PostMapping("deviceRequest/{idRequest}/reject")
    public ResponseEntity<?> rejectRequest(@PathVariable Integer idRequest) {

        DeviceRequestDto updatedRequest = deviceRequest.rejectRequest(idRequest);
        if (updatedRequest == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Solicitud no encontrada");
        }

        return ResponseEntity.ok(updatedRequest);
    }


    
}
