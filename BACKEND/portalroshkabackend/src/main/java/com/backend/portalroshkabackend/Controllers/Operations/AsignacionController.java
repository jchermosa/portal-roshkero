package com.backend.portalroshkabackend.Controllers.Operations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.portalroshkabackend.DTO.Operationes.AsignacionResponseDto;
import com.backend.portalroshkabackend.Services.Operations.Interface.IAsignacionService;

@RestController("asignacionController")
@RequestMapping("api/v1/admin/operatios/asignacion")
public class AsignacionController {

    private final IAsignacionService asignacionService;

    @Autowired
    public AsignacionController(
            IAsignacionService asignacionService) {
        this.asignacionService = asignacionService;
    }

    @GetMapping("")
    public ResponseEntity<Page<AsignacionResponseDto>> getAllAsignacion(
            @PageableDefault(size = 10, sort = "equipos.idEquipo", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<AsignacionResponseDto> asignaciones = asignacionService.getAllAsignacion(pageable);
        return ResponseEntity.ok(asignaciones);
    }

}
