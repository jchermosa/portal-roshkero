package com.backend.portalroshkabackend.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.backend.portalroshkabackend.DTO.DispositivoDto;
import com.backend.portalroshkabackend.Models.Dispositivo;
import com.backend.portalroshkabackend.Models.SolicitudDispositivos;
import com.backend.portalroshkabackend.Services.DispositivoService;
import com.backend.portalroshkabackend.Services.SysAdminService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;




@RestController
@RequestMapping("/api/v1/admin/sysadmin")
public class SysAdminController {

    @Autowired
    private final SysAdminService sysAdminService;
    
    @Autowired
    private final DispositivoService dispositivoService;

    SysAdminController(SysAdminService sysAdminService, DispositivoService dispositivoService) {
        this.sysAdminService = sysAdminService;
        this.dispositivoService = dispositivoService;
    }

    // =========> DISPOSITIVOS <=========
    // Obtener todos las solicitudes aprobadas
   @GetMapping("/getAllAprovedRequests")
   public List<SolicitudDispositivos> getAllAprovedRequest() {

        return sysAdminService.getAllAprovedRequest();
   }

    //Obtener todas las solicitudes   
   @GetMapping("/allRequests")
   public List<SolicitudDispositivos> getAllRequests() {
       return sysAdminService.findAllSolicitudes();
   }
   
    // Listar tipos de dispositivos 
    @GetMapping("allDevices")
    public List<Dispositivo> getAllDevices() {
        // Lógica para obtener y devolver la lista de dispositivos
        return dispositivoService.getAllDevices();
    }    

    //Crear dispositivos 
    @PostMapping("createDevice")
    public Dispositivo createDevice(@Valid @RequestBody DispositivoDto dispositivoDto) {

        return dispositivoService.createDevice(dispositivoDto);
    }

    // Actualizar dispositivos
    @PutMapping("updateDevice/{id}")
    public Dispositivo updateDevice(@PathVariable String id, @RequestBody DispositivoDto dispositivoDto) {
        return dispositivoService.updateDevice(Integer.valueOf(id), dispositivoDto);
    }

    // TODO Desactivar dispositivos
        

    





}
