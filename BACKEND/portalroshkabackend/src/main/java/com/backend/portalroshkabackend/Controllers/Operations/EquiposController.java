package com.backend.portalroshkabackend.Controllers.Operations;

import java.util.List;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.backend.portalroshkabackend.DTO.Operationes.EquiposRequestDto;
import com.backend.portalroshkabackend.DTO.Operationes.EquiposResponseDto;
import com.backend.portalroshkabackend.DTO.Operationes.MetaDatasDto;
import com.backend.portalroshkabackend.DTO.Operationes.UsuarioEquipoRequestDto;
import com.backend.portalroshkabackend.DTO.Operationes.UsuariosEquipoCombinedResponseDto;
import com.backend.portalroshkabackend.DTO.Operationes.UsuariosEquipoResponseDto;
import com.backend.portalroshkabackend.Services.Operations.IEquiposService;
import com.backend.portalroshkabackend.Services.Operations.IMetaDatasService;
import com.backend.portalroshkabackend.Services.Operations.IUsuarioisEquipoService;

import jakarta.validation.Valid;

@RestController("equiposController")
@RequestMapping("/api/v1/admin/operations")
public class EquiposController {

    private final IMetaDatasService metaDatasService;
    private final IEquiposService equiposService;
    private final IUsuarioisEquipoService usuariosEquipoService;

    @Autowired
    public EquiposController(IEquiposService equiposService, IUsuarioisEquipoService usuariosEquipoService,
            IMetaDatasService metaDatasService) {
        this.equiposService = equiposService;
        this.usuariosEquipoService = usuariosEquipoService;
        this.metaDatasService = metaDatasService;
    }

    @GetMapping("/teams")
    public ResponseEntity<Page<EquiposResponseDto>> getAllTeams(
            @PageableDefault(size = 10, sort = "idEquipo", direction = Sort.Direction.ASC) Pageable pageable,
            @RequestParam(required = false, defaultValue = "default") String sortBy) {

        Page<EquiposResponseDto> teams = equiposService.getTeamsSorted(pageable, sortBy);
        return ResponseEntity.ok(teams);
    }

    @GetMapping("/team/{id}")
    public ResponseEntity<EquiposResponseDto> getTeamById(@PathVariable Integer id) {
        EquiposResponseDto team = equiposService.getTeamById(id);
        return ResponseEntity.ok(team);
    }

    @GetMapping("/metadatas") // info for "form Create team"
    public MetaDatasDto getMetaDatas() {
        return metaDatasService.getMetaDatas();
    }

    // ----------------- CREATE -----------------
    @PostMapping("/team")
    public EquiposResponseDto postNewTeam(@Valid @RequestBody EquiposRequestDto equipoRequest) {
        return equiposService.postNewTeam(equipoRequest);
    }

    // ----------------- DELETE -----------------
    @DeleteMapping("/team/{id}")
    public void deleteTeam(@PathVariable int id) {
        equiposService.deleteTeam(id);
    }

    // ----------------- UPDATE -----------------
    // @PutMapping("/team/{id}")
    // public EquiposResponseDto updateTeam(
    // @PathVariable int id,
    // @Valid @RequestBody EquiposRequestDto equipoRequest) {

    // return equiposService.updateTeam(id, equipoRequest);
    // }

    // Asignacion
    @GetMapping("teams/{idEquipo}/users")
    public ResponseEntity<UsuariosEquipoCombinedResponseDto> getUsuariosEquipo(
            @PathVariable Integer idEquipo,
            @PageableDefault(size = 10, sort = "idUsuario", direction = Sort.Direction.ASC) Pageable pageable) {

        UsuariosEquipoCombinedResponseDto response = usuariosEquipoService.getUsuariosEnYFueraDeEquipo(idEquipo,
                pageable);
        return ResponseEntity.ok(response);
    }

    @PostMapping("teams/{idEquipo}/users")
    public ResponseEntity<UsuariosEquipoResponseDto> addUsuarioEquipo(
            @PathVariable Integer idEquipo,
            @RequestBody @Valid UsuarioEquipoRequestDto dto) {

        return ResponseEntity.ok(usuariosEquipoService.addUsuarioToEquipo(idEquipo, dto));
    }

    // @PutMapping("teams/{idEquipo}/users/{idAsignacionUsuario}")
    // public ResponseEntity<UsuarioisResponseDto> updateUsuarioEquipo(
    // @PathVariable Integer idEquipo,
    // @PathVariable Integer idAsignacionUsuario,
    // @RequestBody @Valid UsuarioEquipoUpdateRequestDto dto) {

    // UsuariosEquipoUpdateResponseDto updated =
    // usuariosEquipoService.updateUsuarioEnEquipo(
    // idEquipo,
    // idAsignacionUsuario,
    // dto);
    // return ResponseEntity.ok(updated);
    // }

    // @DeleteMapping("teams/{idEquipo}/users/{idAsignacionUsuario}")
    // public ResponseEntity<Void> deleteUsuarioEquipo(
    // @PathVariable Integer idEquipo,
    // @PathVariable Integer idAsignacionUsuario) {

    // usuariosEquipoService.removeUsuarioDeEquipo(idEquipo, idAsignacionUsuario);
    // return ResponseEntity.noContent().build();
    // }

}
