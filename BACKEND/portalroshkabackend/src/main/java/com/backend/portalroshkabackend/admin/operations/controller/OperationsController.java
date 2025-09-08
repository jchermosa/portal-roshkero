package com.backend.portalroshkabackend.admin.operations.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/v1/admin")
public class OperationsController {

    @GetMapping("/operations/request")
    // view all request (read-only)
    public String getAllRequests() {
        return "1";
    }

    @GetMapping("/operations/rols")
    // show all rols
    public String getAllRols() {
        return "1";
    }

    @GetMapping("/operations/positions")
    // show all positions
    public String getAllPositions() {
        return "1";
    }

    @GetMapping("/operations/teams")
    // show all teams
    public String getAllTeams() {
        return "1";
    }

    @PostMapping("/operations/teams")
    // create a new team
    public String postNewTeams() {
        return "1";
    }

    @DeleteMapping("/operations/teams/{id}")
    // delete a team
    public String deleateTeam() {
        return "1";
    }

    @PutMapping("/operations/teams/{id}")
    // update a team
    public String updateTeam() {
        return "1";
    }
}
