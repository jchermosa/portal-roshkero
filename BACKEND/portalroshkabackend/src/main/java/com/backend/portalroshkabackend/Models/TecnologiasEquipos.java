package com.backend.portalroshkabackend.Models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "tecnologias_equipos")
public class TecnologiasEquipos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tecnologia_equipo")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_tecnologia")
    private Tecnologias tecnologia;

    @ManyToOne
    @JoinColumn(name = "id_equipo")
    private Equipos equipo;
}