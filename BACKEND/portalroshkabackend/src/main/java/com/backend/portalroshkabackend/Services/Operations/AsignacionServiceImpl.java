package com.backend.portalroshkabackend.Services.Operations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.backend.portalroshkabackend.DTO.Operationes.AsignacionResponseDto;
import com.backend.portalroshkabackend.Models.AsignacionUsuario;
import com.backend.portalroshkabackend.Repositories.EquiposRepository;
import com.backend.portalroshkabackend.Repositories.TecnologiaRepository;
import com.backend.portalroshkabackend.Repositories.UserRepository;
import com.backend.portalroshkabackend.Repositories.AsignacionUsuarioRepository;

@Service("operationsAsignacionService")
public class AsignacionServiceImpl implements IAsignacionService {
    // private final EquiposRepository equiposRepository;
    // private final TecnologiaRepository tecnologiaRepository;
    // private final UserRepository userRepository;
    private final AsignacionUsuarioRepository asignacionUsuarioRepository;

    @Autowired
    public AsignacionServiceImpl(
            AsignacionUsuarioRepository asignacionUsuarioRepository) {
        this.asignacionUsuarioRepository = asignacionUsuarioRepository;
    }

    @Override
    public Page<AsignacionResponseDto> getAllAsignacion(Pageable pageable) {
        return asignacionUsuarioRepository.findAll(pageable)
                .map(asignacion -> {
                    AsignacionResponseDto Dto = new AsignacionResponseDto();
                    Dto.setIdAsignacionUsuario(asignacion.getIdAsignacionUsuario());
                    Dto.setFechaEntrada(asignacion.getFechaEntrada());
                    Dto.setFechaFin(asignacion.getFechaFin());
                    Dto.setPorcentajeTrabajo(asignacion.getPorcentajeTrabajo());
                    Dto.setIdUsuario(asignacion.getIdUsuario());
                    Dto.setIdTecnologia(asignacion.getIdTecnologia());
                    Dto.setEquipo(asignacion.getEquipos());
                    Dto.setFechaCreacion(asignacion.getFechaCreacion());
                    return Dto;
                });
    }
}
