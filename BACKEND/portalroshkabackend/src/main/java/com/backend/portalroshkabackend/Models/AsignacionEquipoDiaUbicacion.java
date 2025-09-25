package com.backend.portalroshkabackend.Models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "asignacion_equipo_dia_ubicacion")
public class AsignacionEquipoDiaUbicacion {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "id_asignacion_equipo_dia_ubicacion")
    private Integer idAsignacionEquipoDiaUbicacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_equipo")
    private Equipos equipo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_dia_laboral")
    private DiasLaborales diaLaboral;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ubicacion")
    private Ubicacion ubicacion;
}
