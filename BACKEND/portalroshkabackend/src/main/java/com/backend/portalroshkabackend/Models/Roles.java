package com.backend.portalroshkabackend.Models;

<<<<<<< HEAD
=======

// import java.util.Set;
import java.time.LocalDateTime;
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
// import jakarta.persistence.OneToMany;
// import jakarta.persistence.FetchType;

import jakarta.validation.constraints.NotBlank;
>>>>>>> parent of dca61a3 (se elimino backend)
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "roles")
public class Roles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol")
    private Integer idRol;
<<<<<<< HEAD
    private String nombre;
=======

    @NotBlank
    @Column(name = "nombre", unique = true)
    private String nombre;

//    @OneToMany (mappedBy = "rol", fetch = FetchType.LAZY)
//    private Set<Usuario> usuarios = new HashSet<>();

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;
>>>>>>> parent of dca61a3 (se elimino backend)
    

}
