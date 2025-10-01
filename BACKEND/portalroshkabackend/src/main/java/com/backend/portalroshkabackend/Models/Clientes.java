package com.backend.portalroshkabackend.Models;

import java.time.LocalDateTime;
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
@Table(name = "clientes")
public class Clientes {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCliente;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "nro_telefono")
    private String nroTelefono;
    @Column(name = "correo")
    private String correo;
    @Column(name = "ruc")
    private String ruc;
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;
}
