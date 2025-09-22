package com.backend.portalroshkabackend.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class DispositivoDto {
    private Integer idTipoInventario;
    private String nombre;
    private String detalle;
    private LocalDateTime fechaCreacion;
 
    public Integer getIdTipoInventario() {
        return idTipoInventario;
    }

    public void setIdTipoInventario(Integer idTipoInventario) {
        this.idTipoInventario = idTipoInventario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
