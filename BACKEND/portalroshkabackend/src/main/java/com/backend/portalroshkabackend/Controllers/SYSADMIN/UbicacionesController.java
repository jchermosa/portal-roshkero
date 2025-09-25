package com.backend.portalroshkabackend.Controllers.SYSADMIN;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.portalroshkabackend.DTO.SYSADMIN.UbicacionDto;
import com.backend.portalroshkabackend.Services.SysAdmin.UbicacionService;

import jakarta.validation.Valid;

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
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/v1/admin/sysadmin/ubicaciones")
public class UbicacionesController {

    @Autowired
    private final UbicacionService ubicacionService;

    UbicacionesController(UbicacionService ubicacionService) {
        this.ubicacionService = ubicacionService;
    }

     // ==========> UBICACION <==========


    // Get todas las ubicaciones 
    @GetMapping("/getAll")
    public List<UbicacionDto> getAllUbicaciones() {
        return ubicacionService.getAllUbicaciones();
    }

    // Get ubicacion por id 
    @GetMapping("/{id}")
    public ResponseEntity<UbicacionDto> getUbicacionById(@PathVariable Integer id) {
        return ubicacionService.findByIdUbicacion(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }
    

    // CRUD UBICACIONES

    @PostMapping("/create")
    public UbicacionDto createUbicacion(@Valid @RequestBody UbicacionDto ubicacionDto) {
        return ubicacionService.createUbicacion(ubicacionDto);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<UbicacionDto> updateUbicacion(@PathVariable Integer id, @Valid @RequestBody UbicacionDto ubicacionDto) {
        return ubicacionService.updateUbicacion(id, ubicacionDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @DeleteMapping("/delete/{id}")
    public Void deleteUbicacion(@PathVariable Integer id) {
        ubicacionService.deleteUbicacion(id);
        return null;
    }

    

    
}
