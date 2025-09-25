package com.backend.portalroshkabackend.Models;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "asignacion_equipo_dia_ubicacion")
public class AsignacionEquipoDiaUbicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_asignacion_equipo_dia_ubicacion")
    private Integer idAsignacionEquipoDiaUbicacion;

    @Column(name = "id_equipo", nullable = false)
    private Integer idEquipo;

    @Column(name = "id_dia_laboral", nullable = false)
    private Integer idDiaLaboral;

    @Column(name = "id_ubicacion", nullable = false)
    private Integer idUbicacion;

    // Связь с командой
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_equipo", insertable = false, updatable = false)
    private Equipos equipo;

    // Связь с днем недели
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_dia_laboral", insertable = false, updatable = false)
    private DiaLaboral diaLaboral;

    // Связь с местом
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ubicacion", insertable = false, updatable = false)
    private Ubicacion ubicacion;
}
