package com.backend.portalroshkabackend.Models;

import java.time.LocalDateTime;
// import java.util.List;
// import java.util.ArrayList;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
// import jakarta.persistence.OneToMany;
// import jakarta.persistence.FetchType;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@Entity
@Table(name = "documentos_adjuntos")
public class DocumentoAdjunto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_documento_adjunto")
    private Integer idDocumentoAdjunto;

    @Column(name = "nombre_archivo", nullable = false, length = 500)
    private String nombreArchivo;

    @Column(name = "url_documento", nullable = false, length = 500)
    private String urlDocumento;

    @Column(name = "fecha_subida")
    private LocalDateTime fechaSubida;

//    @OneToMany(mappedBy = "documentoAdjunto", fetch = FetchType.LAZY)
//    private List<Solicitud> solicitudes = new ArrayList<>();

}
