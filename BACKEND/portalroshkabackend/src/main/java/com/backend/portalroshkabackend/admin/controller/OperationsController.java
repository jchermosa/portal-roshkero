package com.backend.portalroshkabackend.admin.controller;

import com.backend.portalroshkabackend.admin.dto.EquiposRequestDto;
import com.backend.portalroshkabackend.admin.dto.CargosResponseDto;
import com.backend.portalroshkabackend.admin.dto.EquiposResponseDto;
import com.backend.portalroshkabackend.admin.dto.RequestResponseDto;
import com.backend.portalroshkabackend.admin.dto.RolesResponseDto;

import jakarta.validation.Valid;

import com.backend.portalroshkabackend.admin.service.IOperationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
public class OperationsController {

    private final IOperationService operationService;

    public OperationsController(IOperationService operationService) {
        this.operationService = operationService;
    }

    // ----------------- READ -----------------
    @GetMapping("/operations/request")
    public List<RequestResponseDto> getAllRequests() {
        return operationService.getAllRequests();
    }

    @GetMapping("/operations/rols")
    public List<RolesResponseDto> getAllRols() {
        return operationService.getAllRols();
    }

    @GetMapping("/operations/positions")
    public List<CargosResponseDto> getAllPositions() {
        return operationService.getAllCargos();
    }

    @GetMapping("/operations/teams")
    public List<EquiposResponseDto> getAllTeams() {
        return operationService.getAllTeams();
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
