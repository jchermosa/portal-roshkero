package com.backend.portalroshkabackend.tools.validator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.portalroshkabackend.Exception.DisponibilidadInsuficienteException;
import com.backend.portalroshkabackend.Exception.NombreDuplicadoException;
import com.backend.portalroshkabackend.Models.Usuario;
import com.backend.portalroshkabackend.Repositories.OP.EquiposRepository;
import com.backend.portalroshkabackend.Repositories.OP.UsuarioisRepository;
import com.backend.portalroshkabackend.DTO.Operationes.UsuarioAsignacionDto;


@Service
public class TeamValidator {

    @Autowired
    private EquiposRepository equiposRepository;
    @Autowired
    private UsuarioisRepository usuarioisRepository;

    // Проверка уникальности имени команды
    public void validateUniqueName(String nombre) {
        if (!equiposRepository.findAllByNombre(nombre.trim()).isEmpty()) {
            throw new NombreDuplicadoException("El nombre ya existe");
        }
    }

    // Проверка и получение лидера
    public Usuario validateLeader(Integer idLider) {
        if (idLider == null)
            return null;
        return usuarioisRepository.findById(idLider)
                .orElseThrow(() -> new RuntimeException("Líder no encontrado"));
    }

    // Проверка пользователей команды
    public Map<Integer, Usuario> validateUsers(List<UsuarioAsignacionDto> usuarios) {
        Map<Integer, Usuario> usuariosValidados = new HashMap<>();
        if (usuarios != null) {
            for (UsuarioAsignacionDto uDto : usuarios) {
                Usuario usuario = usuarioisRepository.findById(uDto.getIdUsuario())
                        .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + uDto.getIdUsuario()));

                if (uDto.getPorcentajeTrabajo() == null)
                    throw new RuntimeException("PorcentajeTrabajo obligatorio para usuario " + usuario.getIdUsuario());

                if (uDto.getFechaEntrada() == null)
                    throw new RuntimeException("FechaEntrada obligatoria para usuario " + usuario.getIdUsuario());

                if (usuario.getDisponibilidad() == null || usuario.getDisponibilidad() < uDto.getPorcentajeTrabajo()) {
                    throw new DisponibilidadInsuficienteException("Usuario " + usuario.getIdUsuario() +
                            " no tiene suficiente disponibilidad. Actual: " +
                            (usuario.getDisponibilidad() == null ? 0 : usuario.getDisponibilidad()) +
                            ", requerido: " + uDto.getPorcentajeTrabajo());
                }

                usuariosValidados.put(usuario.getIdUsuario(), usuario);
            }
        }
        return usuariosValidados;
    }
}
