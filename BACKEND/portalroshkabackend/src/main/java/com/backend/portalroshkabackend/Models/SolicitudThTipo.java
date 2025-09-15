package com.backend.portalroshkabackend.Models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "solicitud_th_tipo")
public class SolicitudThTipo {
    
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Column(name = "id_solicitud_tipo")
    private Integer idSolicitudTipo;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "fecha_creacion")
    private java.sql.Date fechaCreacion;
}
