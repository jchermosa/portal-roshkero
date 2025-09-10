//  Create/Update
package com.backend.portalroshkabackend.DTO;

import jakarta.validation.constraints.NotBlank;

public class EquiposRequestDto {

    @NotBlank(message = "El nombre no puede estar vacio")
    private String nombre;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}