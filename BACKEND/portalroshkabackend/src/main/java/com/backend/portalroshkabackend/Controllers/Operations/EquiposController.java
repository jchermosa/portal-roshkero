package com.backend.portalroshkabackend.Controllers.Operations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.portalroshkabackend.DTO.Operationes.EquiposRequestDto;
import com.backend.portalroshkabackend.DTO.Operationes.EquiposResponseDto;
import com.backend.portalroshkabackend.Services.Operations.IEquiposService;

import jakarta.validation.Valid;

@RestController("equiposController")
@RequestMapping("/api/v1/admin/operations/teams")
public class EquiposController {

    private final IEquiposService equiposService;

    @Autowired
    public EquiposController(IEquiposService equiposService) {
        this.equiposService = equiposService;
    }

    @GetMapping("")
    public ResponseEntity<Page<EquiposResponseDto>> getAllTeams(
            @PageableDefault(size = 10, sort = "idEquipo", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<EquiposResponseDto> teams = equiposService.getAllTeams(pageable);
        return ResponseEntity.ok(teams);
    }

    // ----------------- CREATE -----------------
    @PostMapping("")
    public EquiposResponseDto postNewTeam(@Valid @RequestBody EquiposRequestDto equipoRequest) {
        return equiposService.postNewTeam(equipoRequest);
    }

    // ----------------- DELETE -----------------
    @DeleteMapping("/{id}")
    public void deleteTeam(@PathVariable int id) {
        equiposService.deleteTeam(id);
    }

    // ----------------- UPDATE -----------------
    @PutMapping("/{id}")
    public EquiposResponseDto updateTeam(@PathVariable int id, @Valid @RequestBody EquiposRequestDto equipoRequest) {
        return equiposService.updateTeam(id, equipoRequest);
    }

}
