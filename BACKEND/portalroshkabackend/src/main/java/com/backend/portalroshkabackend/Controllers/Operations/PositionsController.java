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

import com.backend.portalroshkabackend.DTO.Operationes.CargosResponseDto;
import com.backend.portalroshkabackend.Services.Operations.IPositionsService;

@RestController
@RequestMapping("/api/v1/admin/operations/positions")
public class PositionsController {

    private final IPositionsService positionsService;

    @Autowired
    public PositionsController(IPositionsService positionsService) {
        this.positionsService = positionsService;
    }

    @GetMapping("")
    public ResponseEntity<Page<CargosResponseDto>> getAllPositions(
            @PageableDefault(size = 10, sort = "idCargo", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<CargosResponseDto> cargos = positionsService.getAllCargos(pageable);
        return ResponseEntity.ok(cargos);
    }
}
