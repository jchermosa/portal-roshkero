package com.backend.portalroshkabackend.Controllers.SYSADMIN;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.backend.portalroshkabackend.DTO.DispositivoDto;
import com.backend.portalroshkabackend.DTO.SYSADMIN.DeviceDTO;
import com.backend.portalroshkabackend.DTO.SYSADMIN.DeviceTypeDTO;
import com.backend.portalroshkabackend.Services.SysAdmin.DispositivoService;

import jakarta.validation.Valid;

@Controller
public class DispositivoController {


    @Autowired
    private final DispositivoService dispositivoService;

    DispositivoController(DispositivoService dispositivoService) {
        this.dispositivoService = dispositivoService;
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


    // CRUD 

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
}
