package com.backend.portalroshkabackend.Models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    @ManyToOne
    @JoinColumn(name = "id_solicitud_th")
    private SolicitudesTH idSolicitud;

    @Column(name = "url_documento")
    private String urlDocumento;

    @Column(name = "fecha_subida")
    private LocalDateTime fechaSubida;
}
