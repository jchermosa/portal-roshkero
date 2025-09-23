package com.backend.portalroshkabackend.Services.Operations.Interface;

import com.backend.portalroshkabackend.Models.Usuario;

public interface IUsuarioService {
    Usuario getUsuarioById(Integer id);

    void ajustarDisponibilidadConDelta(Usuario usuario, float delta);

    void devolverDisponibilidad(Usuario usuario, float delta);
}
