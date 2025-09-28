package com.backend.portalroshkabackend.Services.Operations.Service.AsignacionUsuarioEquipo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.portalroshkabackend.DTO.Operationes.UsuarioAsignacionDto;
import com.backend.portalroshkabackend.DTO.Operationes.UsuarioisResponseDto;
import com.backend.portalroshkabackend.Repositories.OP.AsignacionUsuarioRepository;
import com.backend.portalroshkabackend.Repositories.OP.UsuarioisRepository;
import com.backend.portalroshkabackend.Services.Operations.Interface.AsignacionUsuarioEquipo.IAsignacionUsuarioEquipoService;

@Service
public class AsignacionUsuarioEquipoServiceImpl implements IAsignacionUsuarioEquipoService{

    @Autowired
    private AsignacionUsuarioRepository asignacionUsuarioRepository;
    @Autowired
    private UsuarioisRepository usuarioisRepository;

    @Override
    public List<UsuarioAsignacionDto> getUsuariosAsignacion(Integer idEquipo) {
        return asignacionUsuarioRepository.findAllByEquipo_IdEquipo(idEquipo)
                .stream()
                .map(asig -> new UsuarioAsignacionDto(
                        asig.getUsuario().getIdUsuario(),
                        asig.getUsuario().getNombre(),
                        asig.getUsuario().getApellido(),
                        asig.getUsuario().getCorreo(),
                        asig.getPorcentajeTrabajo(),
                        asig.getFechaEntrada(),
                        asig.getFechaFin()))
                .toList();
    }

    @Override
    public List<UsuarioisResponseDto> getUsuariosFueraEquipo(Integer idEquipo) {
        return usuarioisRepository.findUsuariosNoEnEquipo(idEquipo)
                .stream()
                .map(u -> new UsuarioisResponseDto(
                        u.getIdUsuario(),
                        u.getNombre(),
                        u.getApellido(),
                        u.getCorreo(),
                        u.getDisponibilidad()))
                .toList();
    }
}
