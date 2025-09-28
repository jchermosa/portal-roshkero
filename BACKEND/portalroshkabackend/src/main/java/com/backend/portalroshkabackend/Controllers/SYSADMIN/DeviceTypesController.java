package com.backend.portalroshkabackend.Controllers.SYSADMIN;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.portalroshkabackend.DTO.SYSADMIN.DeviceTypeDTO;
import com.backend.portalroshkabackend.Services.SysAdmin.DeviceTypeService;

@RestController
@RequestMapping("/api/v1/admin/sysadmin/deviceTypes")
public class DeviceTypesController {

    @Autowired
    private DeviceTypeService deviceTypeService;

    @GetMapping("/allTypes")
    public ResponseEntity<List<DeviceTypeDTO>> getAllDeviceTypes() {
        List<DeviceTypeDTO> deviceTypeDTOs = deviceTypeService.getAllDeviceTypes();
        return ResponseEntity.ok(deviceTypeDTOs);
    }

    @GetMapping("/getTypeDevice/{id}")
    public ResponseEntity<DeviceTypeDTO> getDeviceTypeById(@PathVariable Integer id) {
        try {
            DeviceTypeDTO deviceType = deviceTypeService.getDeviceTypeById(id);
            return ResponseEntity.ok(deviceType);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/createTypeDevice")
    public ResponseEntity<DeviceTypeDTO> createDeviceType(@RequestBody DeviceTypeDTO deviceTypeDTO) {
        DeviceTypeDTO savedDeviceType = deviceTypeService.createDeviceType(deviceTypeDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDeviceType);
    }

    @PutMapping("/updateTypeDevice/{id}")
    public ResponseEntity<DeviceTypeDTO> updateDeviceType(@PathVariable Integer id, @RequestBody DeviceTypeDTO deviceTypeDTO) {
        // Set the ID to ensure we're updating the correct entity
        deviceTypeDTO.setIdTipoDispositivo(id);
        DeviceTypeDTO updatedDeviceType = deviceTypeService.updateDeviceType(deviceTypeDTO);
        return ResponseEntity.ok(updatedDeviceType);
    }

    @DeleteMapping("/deleteTypeDevice/{id}")
    public ResponseEntity<Void> deleteDeviceType(@PathVariable Integer id) {
        deviceTypeService.deleteDeviceType(id);
        return ResponseEntity.noContent().build();
    }
}
