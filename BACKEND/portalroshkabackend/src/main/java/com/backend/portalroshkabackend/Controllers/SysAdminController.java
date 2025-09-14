package com.backend.portalroshkabackend.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.portalroshkabackend.Models.SolicitudDispositivos;
import com.backend.portalroshkabackend.Services.SysAdminService;

@RestController
@RequestMapping("/api/v1/admin/sysadmin")
public class SysAdminController {

    @Autowired
    private final SysAdminService sysAdminService;

    SysAdminController(SysAdminService sysAdminService) {
        this.sysAdminService = sysAdminService;
    }

    // Obtener todos las solicitudes aprobadas
   @GetMapping("/test")
   public List<SolicitudDispositivos> getAllAprovedRequest() {

        return sysAdminService.getAllAprovedRequest();
   }


}
