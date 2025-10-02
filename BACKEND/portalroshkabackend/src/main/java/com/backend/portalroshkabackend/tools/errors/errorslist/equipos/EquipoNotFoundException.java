package com.backend.portalroshkabackend.tools.errors.errorslist.equipos;

public class EquipoNotFoundException  extends RuntimeException {
    public EquipoNotFoundException(int id) {
        super("Equipo con ID " + id + " no encontrado.");
    }
}