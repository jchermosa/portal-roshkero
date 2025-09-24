package com.backend.portalroshkabackend.Controllers.SYSADMIN;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.portalroshkabackend.DTO.DeviceRequestDto;
import com.backend.portalroshkabackend.DTO.DispositivoDto;
import com.backend.portalroshkabackend.DTO.SYSADMIN.DeviceDTO;
import com.backend.portalroshkabackend.DTO.SYSADMIN.DeviceTypeDTO;
import com.backend.portalroshkabackend.DTO.SYSADMIN.RequestDTO;
import com.backend.portalroshkabackend.DTO.SYSADMIN.UbicacionDto;
import com.backend.portalroshkabackend.Models.Solicitud;
import com.backend.portalroshkabackend.DTO.DispositivoAsignadoDto;
import com.backend.portalroshkabackend.Services.SysAdmin.DeviceRequest;
import com.backend.portalroshkabackend.Services.SysAdmin.DispositivoService;
import com.backend.portalroshkabackend.Services.SysAdmin.SysAdminService;
import com.backend.portalroshkabackend.Services.SysAdmin.UbicacionService;
import com.backend.portalroshkabackend.Services.DispositivoAsignadoService;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;





@RestController
@RequestMapping("/api/v1/sysadmin")
public class SysAdminController {

    @Autowired
    private final SysAdminService sysAdminService;

    @Autowired
    private final DeviceRequest deviceRequest;

    @Autowired
    private final DispositivoService dispositivoService;

    @Autowired 
    private final DispositivoAsignadoService dispositivoAsignadoService;


    SysAdminController(SysAdminService sysAdminService, DispositivoService dispositivoService, DispositivoAsignadoService dispositivoAsignadoService, DeviceRequest deviceRequest) {
        this.sysAdminService = sysAdminService;
        this.dispositivoService = dispositivoService;
        this.dispositivoAsignadoService = dispositivoAsignadoService;
        this.deviceRequest = deviceRequest;
    }

    // =========> DISPOSITIVOS <=========

    // =======> LISTANDO

    // Obtener los tipos de dispositivos 
    @GetMapping("/getDeviceTypes")
    public List<DeviceTypeDTO> getDeviceTypes() {
        return dispositivoService.getAllTypes();
    }

    // Listar los dispositivos 
    @GetMapping("/allDevices")
    public List<DeviceDTO> getAllDevices() {
        return dispositivoService.getAllDevices();
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


    // CRUD

        //Crear dispositivos 
    @PostMapping("createDevice")
    public DispositivoDto createDevice(@Valid @RequestBody DispositivoDto dispositivoDto) {

        return dispositivoService.createDevice(dispositivoDto);
    }

    // Actualizar dispositivos
    @PutMapping("updateDevice/{id}")
    public DispositivoDto updateDevice(@PathVariable String id, @RequestBody DispositivoDto dispositivoDto) {
        return dispositivoService.updateDevice(Integer.valueOf(id), dispositivoDto);
    }
    
       // TODO Desactivar dispositivos
        
    @DeleteMapping("deleteDevice/{id}")
    public void deleteDevice(@PathVariable Integer id) {
        sysAdminService.deleteDispositivo(id);
    }


    // Crear una asignacion 

    @PostMapping("createAssignment")
    public DispositivoAsignadoDto createAssignment(@Valid @RequestBody DispositivoAsignadoDto inventarioAsignadoDto) {
        return dispositivoAsignadoService.crearInventarioAsignado(inventarioAsignadoDto);
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


   
    /* 
    @PostMapping("deviceRequest/{id}")
    public ResponseEntity<?> acceptRequest(@PathVariable int idRequest, @Valid @RequestBody DeviceRequestDto deviceRequestDto) {

        DeviceRequestDto updatedRequest = deviceRequest.acceptRequest(idRequest, deviceRequestDto);
        if (updatedRequest == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Solicitud no encontrada");
        }

        return ResponseEntity.ok(updatedRequest);
    }
    */

}
