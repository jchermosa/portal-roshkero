package com.backend.portalroshkabackend.Models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "herramienta_usuario")
@Data
@NoArgsConstructor
public class HerramientaUsuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_herramienta_usuario")
    private Integer idHerramientaUsuario;

    @ManyToOne
    @JoinColumn(name = "id_herramienta")
    private Herramientas idHerramienta;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario idUsuario;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

}
