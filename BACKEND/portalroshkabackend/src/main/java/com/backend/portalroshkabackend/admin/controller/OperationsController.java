package com.backend.portalroshkabackend.admin.controller;

import com.backend.portalroshkabackend.common.model.Cargos;
import com.backend.portalroshkabackend.common.model.Equipos;
import com.backend.portalroshkabackend.common.model.Request;
import com.backend.portalroshkabackend.common.model.Roles;
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
    public List<Equipos> getAllTeams() {
        return operationService.getAllTeams();
    }

    // ----------------- CREATE -----------------
    @PostMapping("/operations/teams")
    public Equipos postNewTeam(@RequestBody Equipos equipo) {
        return operationService.postNewTeam(equipo);
    }

    // ----------------- DELETE -----------------
    @DeleteMapping("/operations/teams/{id}")
    public void deleteTeam(@PathVariable int id) {
        operationService.deleteTeam(id);
    }

    // ----------------- UPDATE -----------------
    @PutMapping("/operations/teams/{id}")
    public Equipos updateTeam(@PathVariable int id, @RequestBody Equipos equipo) {
        return operationService.updateTeam(id, equipo);
    }
}
