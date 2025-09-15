package com.backend.portalroshkabackend.Models;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "documentos_permisos")
@Data
@NoArgsConstructor
public class DocumentosPermisos {
    
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    @Column(name = "id_documento")
    private Integer idDocumento;

    @Column(name = "id_solicitud")
    private Integer idSolicitud;

    @Column(name = "url_documento")
    private String urlDocumento;

    @Column(name = "fecha_subida")
    private Date fechaSubida;
}
