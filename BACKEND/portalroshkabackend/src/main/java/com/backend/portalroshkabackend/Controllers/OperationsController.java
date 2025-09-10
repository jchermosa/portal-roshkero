package com.backend.portalroshkabackend.Controllers;

import jakarta.validation.Valid;

import com.backend.portalroshkabackend.DTO.CargosResponseDto;
import com.backend.portalroshkabackend.DTO.EquiposRequestDto;
import com.backend.portalroshkabackend.DTO.EquiposResponseDto;
import com.backend.portalroshkabackend.DTO.RequestResponseDto;
import com.backend.portalroshkabackend.DTO.RolesResponseDto;
import com.backend.portalroshkabackend.Services.IOperationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
