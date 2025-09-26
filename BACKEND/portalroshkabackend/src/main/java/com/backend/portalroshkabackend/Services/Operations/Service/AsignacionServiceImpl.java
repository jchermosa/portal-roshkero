package com.backend.portalroshkabackend.Services.Operations.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.backend.portalroshkabackend.DTO.Operationes.AsignacionResponseDto;
import com.backend.portalroshkabackend.Repositories.OP.AsignacionUsuarioRepository;
import com.backend.portalroshkabackend.Services.Operations.Interface.IAsignacionService;

@Service("operationsAsignacionService")
public class AsignacionServiceImpl implements IAsignacionService {
    @Autowired private AsignacionUsuarioRepository asignacionUsuarioRepository;

    @Override
    public Page<AsignacionResponseDto> getAllAsignacion(Pageable pageable) {
        return asignacionUsuarioRepository.findAll(pageable)
                .map(asignacion -> {
                    AsignacionResponseDto Dto = new AsignacionResponseDto();
                    Dto.setIdAsignacionUsuarioEquipo(asignacion.getIdAsignacionUsuarioEquipo());
                    Dto.setFechaEntrada(asignacion.getFechaEntrada());
                    Dto.setFechaFin(asignacion.getFechaFin());
                    Dto.setPorcentajeTrabajo(asignacion.getPorcentajeTrabajo());
                    Dto.setIdUsuario(asignacion.getUsuario());
                    Dto.setEquipo(asignacion.getEquipo());
                    Dto.setFechaCreacion(asignacion.getFechaCreacion());
                    return Dto;
                });
    }
}
