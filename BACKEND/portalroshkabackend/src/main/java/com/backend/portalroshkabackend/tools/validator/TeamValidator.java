package com.backend.portalroshkabackend.tools.validator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.portalroshkabackend.Exception.DisponibilidadInsuficienteException;
import com.backend.portalroshkabackend.Exception.NombreDuplicadoException;
import com.backend.portalroshkabackend.Exception.UsuarioFechasInvalidasException;
import com.backend.portalroshkabackend.Exception.ValidateFechaInicioAntesDeLimiteException;
import com.backend.portalroshkabackend.Models.Usuario;
import com.backend.portalroshkabackend.Repositories.OP.EquiposRepository;
import com.backend.portalroshkabackend.Repositories.OP.UsuarioisRepository;
import com.backend.portalroshkabackend.DTO.Operationes.EquiposRequestDto;
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

    public void validateFechaInicioAntesDeLimite(EquiposRequestDto dto) {
        LocalDate inicio = dto.getFechaInicio();
        LocalDate limite = dto.getFechaLimite();

        if (inicio != null && limite != null && inicio.isAfter(limite)) {
            throw new ValidateFechaInicioAntesDeLimiteException(
                    "La fecha de inicio no puede ser después de la fecha límite");
        }
    }

    public void validateFechasUsuarios(List<UsuarioAsignacionDto> usuarios) {
        List<String> errores = new ArrayList<>();

        for (UsuarioAsignacionDto u : usuarios) {
            String nombre = (u.getNombre() != null ? u.getNombre() : "Usuario") +
                    (u.getApellido() != null ? " " + u.getApellido() : "");
            if (u.getFechaEntrada() == null) {
                errores.add("No se ingresó la fecha de entrada para " + nombre);
            }

            if (u.getFechaEntrada() != null && u.getFechaFin() != null &&
                    u.getFechaEntrada().isAfter(u.getFechaFin())) {
                errores.add("Fechas incorrectas para " + nombre +
                        ": la fecha de fin (" + u.getFechaFin() + ") es anterior a la fecha de entrada ("
                        + u.getFechaEntrada() + ")");
            }
        }
        if (!errores.isEmpty()) {
            throw new UsuarioFechasInvalidasException(errores);
        }
    }


}
