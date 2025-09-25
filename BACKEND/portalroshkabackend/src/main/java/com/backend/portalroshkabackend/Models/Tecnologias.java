package com.backend.portalroshkabackend.Models;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
// import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
// import java.util.Set;
// import java.util.HashSet;
import java.time.LocalDateTime;
// import jakarta.persistence.FetchType;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "tecnologias")
public class Tecnologias {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tecnologia")
    private Integer idTecnologia;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

//    @ManyToMany(mappedBy = "tecnologias", fetch = FetchType.LAZY)
//    private Set<Usuario> usuarios = new HashSet<>();
//
//    @ManyToMany(mappedBy = "tecnologias", fetch = FetchType.LAZY)
//    private Set<Equipos> equipos = new HashSet<>();

}
