package com.backend.portalroshkabackend.Controllers.SYSADMIN;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backend.portalroshkabackend.DTO.SYSADMIN.DeviceDTO;
import com.backend.portalroshkabackend.DTO.SYSADMIN.DeviceTypeDTO;
import com.backend.portalroshkabackend.Services.SysAdmin.DispositivoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/admin/sysadmin/devices")
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
    public Page<DeviceTypeDTO> getDeviceTypes(Pageable pageable) {
        return dispositivoService.getAllTypes(pageable);
    }

    // Listar los dispositivos 
    @GetMapping("/allDevices")
    public Page<DeviceDTO> getAllDevices(Pageable pageable) {
        return dispositivoService.getAllDevices(pageable);
    }  

    // Listar los dispositivos sin duenho
    // Listar los dispositivos sin dueño
    @GetMapping("/allDevicesWithoutOwner")
    public Page<DeviceDTO> getAllDevicesWithoutOwner(
        @RequestParam(required = false, defaultValue = "default") String sortBy,
        @RequestParam(required = false) String filterValue,
        Pageable pageable) {
        return dispositivoService.getAllDevicesWithoutOwner(pageable, filterValue, sortBy);
    }



    // Listar dispositivos dependiendo del tipo 


    // CRUD 

    // CRUD

        //Crear dispositivos 
    @PostMapping("/create")
    public DeviceDTO createDevice(@Valid @RequestBody DeviceDTO dispositivoDto) {

        return dispositivoService.createDevice(dispositivoDto);
    }

    // Actualizar dispositivos
    @PutMapping("/update/{id}")
    public DeviceDTO updateDevice(@PathVariable String id, @RequestBody DeviceDTO dispositivoDto) {
        return dispositivoService.updateDevice(Integer.valueOf(id), dispositivoDto);
    }
    
       // TODO Desactivar dispositivos
        
    @DeleteMapping("/delete/{id}")
    public void deleteDevice(@PathVariable Integer id) {
        dispositivoService.deleteDeviceById(id);
    }
}
