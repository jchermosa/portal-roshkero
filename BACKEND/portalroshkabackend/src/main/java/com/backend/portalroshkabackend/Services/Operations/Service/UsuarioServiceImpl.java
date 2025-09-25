package com.backend.portalroshkabackend.Services.Operations.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.portalroshkabackend.Models.Usuario;
import com.backend.portalroshkabackend.Repositories.OP.UsuarioisRepository;
import com.backend.portalroshkabackend.Services.Operations.Interface.IUsuarioService;

@Service
public class UsuarioServiceImpl implements IUsuarioService {

    @Autowired
    private UsuarioisRepository usuarioRepository;

    public Usuario getUsuarioById(Integer id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario not found: " + id));
    }

    public void ajustarDisponibilidadConDelta(Usuario usuario, float delta) {
        if (delta > 0) {
            // Увеличиваем нагрузку → вычитаем из disponibilidad
            if (usuario.getDisponibilidad() == null || usuario.getDisponibilidad() < delta) {
                throw new RuntimeException(
                        "Usuario " + usuario.getIdUsuario() +
                                " no tiene suficiente disponibilidad. Actual: " + usuario.getDisponibilidad() +
                                ", requerido: " + delta);
            }
            usuario.setDisponibilidad(usuario.getDisponibilidad() - (int) delta);
        } else if (delta < 0) {
            // Уменьшение нагрузки → возвращаем обратно
            usuario.setDisponibilidad(usuario.getDisponibilidad() + (int) (-delta));
        }
        // delta == 0 → ничего не делаем
        usuarioRepository.save(usuario);
    }

    public void devolverDisponibilidad(Usuario usuario, float porcentaje) {
        usuario.setDisponibilidad(usuario.getDisponibilidad() + (int) porcentaje);
        usuarioRepository.save(usuario);
    }
}