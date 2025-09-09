package com.backend.portalroshkabackend.admin.controller;

import com.backend.portalroshkabackend.admin.dto.EquipoRequestDto;
import com.backend.portalroshkabackend.admin.dto.EquipoResponseDto;

import com.backend.portalroshkabackend.common.model.Cargos;
import com.backend.portalroshkabackend.common.model.Request;
import com.backend.portalroshkabackend.common.model.Roles;

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
    public List<Request> getAllRequests() {
        return operationService.getAllRequests();
    }

    @GetMapping("/operations/rols")
    public List<Roles> getAllRols() {
        return operationService.getAllRols();
    }

    @GetMapping("/operations/positions")
    public List<Cargos> getAllPositions() {
        return operationService.getAllCargos();
    }

    @GetMapping("/operations/teams")
    public List<EquipoResponseDto> getAllTeams() {
        return operationService.getAllTeams();
    }

    // ----------------- CREATE -----------------
    @PostMapping("/operations/teams")
    public EquipoResponseDto postNewTeam(@Valid @RequestBody EquipoRequestDto equipoRequest) {
        return operationService.postNewTeam(equipoRequest);
    }

    // ----------------- DELETE -----------------
    @DeleteMapping("/operations/teams/{id}")
    public void deleteTeam(@PathVariable int id) {
        operationService.deleteTeam(id);
    }

    // ----------------- UPDATE -----------------
    @PutMapping("/operations/teams/{id}")
    public EquipoResponseDto updateTeam(@PathVariable int id, @Valid @RequestBody EquipoRequestDto equipoRequest) {
        return operationService.updateTeam(id, equipoRequest);
    }
}
