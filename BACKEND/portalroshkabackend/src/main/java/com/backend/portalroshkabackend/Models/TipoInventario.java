package com.backend.portalroshkabackend.Models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "tipo_inventario")
public class TipoInventario {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_inventario")
    private Integer idInventario;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "detalle")
    private String detalle;   

    @Column(name = "fecha_creacion")
    private java.util.Date fechaCreacion; 


}
