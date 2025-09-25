package com.backend.portalroshkabackend.Models;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "vacaciones_asignadas")
public class VacacionesAsignadas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vacaciones_asignadas")
    private Integer idVacacionesAsignadas;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_solicitud", nullable = false, unique = true)
    private Solicitud solicitud;

    @Column(name = "dias_utilizados", nullable = false)
    private Integer diasUtilizados;

    @Column(name = "confirmacion_th")
    private Boolean confirmacionTH = false;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;
}
