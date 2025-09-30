package com.backend.portalroshkabackend.Controllers.TeamLeader;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.portalroshkabackend.DTO.TeamLeaderDTO.SolTeamLeaderDTO;
import com.backend.portalroshkabackend.DTO.TeamLeaderDTO.SolicitudRespuestaDto;
// import com.backend.portalroshkabackend.DTO.UsuarioDTO.SolicitudUserDto;
import com.backend.portalroshkabackend.Services.TeamLeaderServicio.TeamLeaderService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/api/v1/teamleader")
public class TeamLeaderController {

    @Autowired
    private TeamLeaderService teamLeaderService;

    @GetMapping("/users/requests/getall")
    public ResponseEntity<List<SolTeamLeaderDTO>> getSolicitudesLiderActual() {
        List<SolTeamLeaderDTO> solicitudes = teamLeaderService.getSolicitudesLiderActual();
        return ResponseEntity.ok(solicitudes);
    }

    @PostMapping("/users/requests/{idSolicitud}/accept")
    public ResponseEntity<SolicitudRespuestaDto> acceptRequest(@PathVariable int idSolicitud) {

        SolicitudRespuestaDto respuesta = teamLeaderService.acceptRequest(idSolicitud);

        return ResponseEntity.ok(respuesta); // Placeholder response
    }

    @PostMapping("/users/requests/{idSolicitud}/reject")
    public ResponseEntity<SolicitudRespuestaDto> rejectRequest(@PathVariable int idSolicitud) {

        SolicitudRespuestaDto respuesta = teamLeaderService.rejectRequest(idSolicitud);

        return ResponseEntity.ok(respuesta); // Placeholder response
    }
    

    
}
