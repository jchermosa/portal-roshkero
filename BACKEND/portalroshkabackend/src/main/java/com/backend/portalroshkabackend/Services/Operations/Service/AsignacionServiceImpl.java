package com.backend.portalroshkabackend.Services.Operations.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.backend.portalroshkabackend.DTO.Operationes.AsignacionResponseDto;
import com.backend.portalroshkabackend.DTO.Operationes.UbicacionDiaDto;
import com.backend.portalroshkabackend.Services.Operations.Interface.IAsignacionService;
import com.backend.portalroshkabackend.Repositories.AsignacionUbicacionDiaRepository;
import com.backend.portalroshkabackend.Repositories.AsignacionUsuarioRepository;

@Service("operationsAsignacionService")
public class AsignacionServiceImpl implements IAsignacionService {
    @Autowired
    private AsignacionUsuarioRepository asignacionUsuarioRepository;
    @Autowired
    private AsignacionUbicacionDiaRepository asignacionUbicacionDiaRepository;

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

    @Override
    public List<UbicacionDiaDto> getUbicacionesDiasLibresForEquipo(Integer idEquipo) {
        return asignacionUbicacionDiaRepository.findUbicacionesDiasLibresForEquipo(idEquipo)
                .stream()
                .map(row -> new UbicacionDiaDto(
                        (Integer) row[0],
                        (String) row[1],
                        (Integer) row[2],
                        (String) row[3]))
                .toList();
    }
}
