package com.backend.portalroshkabackend.Controllers.SYSADMIN;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.backend.portalroshkabackend.DTO.SYSADMIN.DeviceRequestDto;
import com.backend.portalroshkabackend.DTO.SYSADMIN.RequestDTO;
import com.backend.portalroshkabackend.Services.SysAdmin.DeviceRequest;
import com.backend.portalroshkabackend.Services.SysAdmin.SysAdminService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;





@RestController
@RequestMapping("/api/v1/sysadmin")
public class SysAdminController {

    @Autowired
    private final SysAdminService sysAdminService;

    @Autowired
    private final DeviceRequest deviceRequest;


    SysAdminController(SysAdminService sysAdminService, DeviceRequest deviceRequest) {
        this.sysAdminService = sysAdminService;
        this.deviceRequest = deviceRequest;
    }




    // Obtener todos las solicitudes aprobadas
   @GetMapping("/getAllApprovedRequests")
   public List<RequestDTO> getAllApprovedRequest() {
        return sysAdminService.getAllApprovedRequest();
   }

    // Obtener todas las solicitudes rechazadas
    @GetMapping("/getAllRejectedRequests")
    public List<RequestDTO> getAllRejectedRequest() {
        return sysAdminService.getAllRejectedRequest();
    }

    // Obtener todas las solicitudes pendientes
    @GetMapping("/getAllPendingRequests")
    public List<RequestDTO> getAllPendingRequest() {
        return sysAdminService.getAllPendingRequest();
    }


    // ======> SOLICITUDES DE DISPOSITIVOS <========

    //Obtener todas las solicitudes   
   @GetMapping("/allRequests")
   public List<RequestDTO> getAllRequests() {
       return sysAdminService.findAllSolicitudes();
   }


    @PostMapping("deviceRequest/{id}/accept")
    public ResponseEntity<?> acceptRequest(@PathVariable Integer idRequest) {

        DeviceRequestDto updatedRequest = deviceRequest.acceptRequest(idRequest);
        if (updatedRequest == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Solicitud no encontrada");
        }

        return ResponseEntity.ok(updatedRequest);
    }

    @PostMapping("deviceRequest/{id}/reject")
    public ResponseEntity<?> rejectRequest(@PathVariable Integer idRequest) {

        DeviceRequestDto updatedRequest = deviceRequest.rejectRequest(idRequest);
        if (updatedRequest == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Solicitud no encontrada");
        }

        return ResponseEntity.ok(updatedRequest);
    }


}
