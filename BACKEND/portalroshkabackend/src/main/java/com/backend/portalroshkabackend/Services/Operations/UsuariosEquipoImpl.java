package com.backend.portalroshkabackend.Services.Operations;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.backend.portalroshkabackend.DTO.Operationes.UsuarioisResponseDto;
import com.backend.portalroshkabackend.DTO.Operationes.UsuariosEquipoResponseDto;
import com.backend.portalroshkabackend.Models.Equipos;
import com.backend.portalroshkabackend.Models.Usuario;
import com.backend.portalroshkabackend.Repositories.AsignacionUsuarioRepository;

@Service
public class UsuariosEquipoImpl implements IUsuarioisEquipoService {
    private final AsignacionUsuarioRepository asignacionUsuarioRepository;

    @Autowired
    public UsuariosEquipoImpl(AsignacionUsuarioRepository asignacionUsuarioRepository) {
        this.asignacionUsuarioRepository = asignacionUsuarioRepository;
    }

    public Page<UsuariosEquipoResponseDto> getAllUsuariosEquipo(Integer idEquipo, Pageable pageable) {
        return asignacionUsuarioRepository.findByEquiposIdEquipo(idEquipo,pageable)
                .map(asignacion -> {
                    UsuariosEquipoResponseDto dto = new UsuariosEquipoResponseDto();
                    dto.setIdUsuario(asignacion.getIdUsuario());
                    return dto;
                });
    }

}
