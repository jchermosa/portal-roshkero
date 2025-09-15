package com.backend.portalroshkabackend.Models;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@NoArgsConstructor
@Data
@Table(name = "solicitud_lideres")
public class SolicitudLideres {


    @Id
    @Column(name = "id_solicitud")
    private Integer idSolicitud;

    @Id
    @Column(name = "id_lider")
    private Integer idLider;


    @Column(name = "fecha_asignacion")
    private Date finVigencia;

    
}
