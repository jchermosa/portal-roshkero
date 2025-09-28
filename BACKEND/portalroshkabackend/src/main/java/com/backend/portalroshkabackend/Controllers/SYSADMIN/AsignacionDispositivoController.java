package com.backend.portalroshkabackend.Controllers.SYSADMIN;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.backend.portalroshkabackend.DTO.SYSADMIN.DeviceAssignmentDTO;
import com.backend.portalroshkabackend.Services.SysAdmin.DeviceAssignmentService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
@RequestMapping("/api/v1/admin/sysadmin/deviceAssignments")
public class AsignacionDispositivoController {

    @Autowired
    private final DeviceAssignmentService deviceAssignmentService;

    AsignacionDispositivoController(DeviceAssignmentService deviceAssignmentService) {
        this.deviceAssignmentService = deviceAssignmentService;
    }


    // Listar las asignaciones de dispositivos 
    @GetMapping("/listAssignments")
    public Page<DeviceAssignmentDTO> listarAsignaciones(Pageable pageable) {

        return deviceAssignmentService.listarAsignaciones(pageable);

    }
    


    // Crear una nueva asignacion de dispositivo
    @PostMapping("/createAssignment")
    public DeviceAssignmentDTO crearAsignacion(@RequestBody DeviceAssignmentDTO deviceAssignmentDTO) {
        return deviceAssignmentService.crearAsignacion(deviceAssignmentDTO);    
    }


    // Actualizar una asignacion de dispositivo
    @PutMapping("/updateAssignment/{id}")
    public DeviceAssignmentDTO actualizarAsignacion(@RequestBody DeviceAssignmentDTO deviceAssignmentDTO, @PathVariable Integer id) {
        return deviceAssignmentService.actualizarAsignacion(id, deviceAssignmentDTO);
    }

    // Eliminar una asignacion de dispositivo
    @DeleteMapping("/deleteAssignment/{id}")
    public void eliminarAsignacion(@PathVariable Integer id) {
        deviceAssignmentService.eliminarAsignacion(id);
    }


}
