package com.backend.portalroshkabackend.Models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "herramienta_usuario")
@Data
@NoArgsConstructor
public class HerramientaUsuario {
    

    @Column(name = "id_herramienta")
    private Integer idHerramienta;

    @Column(name = "id_usuario")
    private Integer idUsuario;

    @Column(name = "fecha_asignacion")
    private java.sql.Date fechaAsignacion;

}
