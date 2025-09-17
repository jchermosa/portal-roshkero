package com.backend.portalroshkabackend.Controllers;

import jakarta.validation.Valid;

import com.backend.portalroshkabackend.DTO.CargosResponseDto;
import com.backend.portalroshkabackend.DTO.EquiposRequestDto;
import com.backend.portalroshkabackend.DTO.EquiposResponseDto;
import com.backend.portalroshkabackend.DTO.RequestResponseDto;
import com.backend.portalroshkabackend.DTO.RolesResponseDto;
import com.backend.portalroshkabackend.Services.IOperationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@RestController
@RequestMapping("/api/v1/admin")
public class OperationsController {

    private final IOperationService operationService;

    @Autowired
    public OperationsController(IOperationService operationService) {
        this.operationService = operationService;
    }

    // ----------------- READ -----------------
    @GetMapping("/operations/request")
    public ResponseEntity<Page<RequestResponseDto>> getAllRequests(
            @PageableDefault(size = 10, sort = "idSolicitud", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<RequestResponseDto> requests = operationService.getAllRequests(pageable);

        return ResponseEntity.ok(requests);
    }

    @GetMapping("/operations/rols")
    public ResponseEntity<Page<RolesResponseDto>> getAllRols(
            @PageableDefault(size = 10, sort = "idRol", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<RolesResponseDto> rols = operationService.getAllRols(pageable);
        return ResponseEntity.ok(rols);
    }

    @GetMapping("/operations/positions")
    public ResponseEntity<Page<CargosResponseDto>> getAllPositions(
            @PageableDefault(size = 10, sort = "idCargo", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<CargosResponseDto> cargos = operationService.getAllCargos(pageable);
        return ResponseEntity.ok(cargos);
    }

    @GetMapping("/operations/teams")
    public ResponseEntity<Page<EquiposResponseDto>> getAllTeams(
            @PageableDefault(size = 10, sort = "idEquipo", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<EquiposResponseDto> teams = operationService.getAllTeams(pageable);
        return ResponseEntity.ok(teams);
    }

    // ----------------- CREATE -----------------
    @PostMapping("/operations/teams")
    public EquiposResponseDto postNewTeam(@Valid @RequestBody EquiposRequestDto equipoRequest) {
        return operationService.postNewTeam(equipoRequest);
    }

    // ----------------- DELETE -----------------
    @DeleteMapping("/operations/teams/{id}")
    public void deleteTeam(@PathVariable int id) {
        operationService.deleteTeam(id);
    }

    // ----------------- UPDATE -----------------
    @PutMapping("/operations/teams/{id}")
    public EquiposResponseDto updateTeam(@PathVariable int id, @Valid @RequestBody EquiposRequestDto equipoRequest) {
        return operationService.updateTeam(id, equipoRequest);
    }
}
