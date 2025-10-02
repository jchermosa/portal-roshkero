package com.backend.portalroshkabackend.Models;

<<<<<<< HEAD
import java.time.LocalDateTime;

=======

import java.time.LocalDate;
// import java.util.Set;
// import java.util.HashSet;
>>>>>>> parent of dca61a3 (se elimino backend)
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
<<<<<<< HEAD
=======
// import jakarta.persistence.FetchType;
// import jakarta.persistence.OneToMany;
>>>>>>> parent of dca61a3 (se elimino backend)
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "cargos")
public class Cargos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cargo")
    private Integer idCargo;

<<<<<<< HEAD
    private String nombre;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;
=======
    @Column(name = "nombre")
    private String nombre;

//    @OneToMany (mappedBy = "cargo", fetch = FetchType.LAZY)
//    private Set<Usuario> usuarios = new HashSet<>();

    @Column(name = "fecha_creacion")
    private LocalDate fechaCreacion;
>>>>>>> parent of dca61a3 (se elimino backend)


}
