package com.backend.portalroshkabackend.Controllers.Operations;

import org.springframework.web.bind.annotation.RestController;

import com.backend.portalroshkabackend.DTO.Operationes.UbicacionDiaDto;
import com.backend.portalroshkabackend.Services.Operations.Interface.IUbicacionSevice;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController("ubicacionController")
@RequestMapping("/api/v1/admin/operations")
public class UbicacionController {

    private final IUbicacionSevice ubicacionService;

    public UbicacionController(IUbicacionSevice ubicacionService) {
        this.ubicacionService = ubicacionService;
    }

    @GetMapping("/ubicacion")
    public List<UbicacionDiaDto> getAllUbicacion() {
        return ubicacionService.getAllUbicacion();
    }

}
