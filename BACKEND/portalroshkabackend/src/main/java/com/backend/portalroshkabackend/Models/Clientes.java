package com.backend.portalroshkabackend.Models;

<<<<<<< HEAD
import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
=======
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.HashSet;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
// import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
// import jakarta.persistence.OneToMany;
>>>>>>> parent of dca61a3 (se elimino backend)
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "clientes")
public class Clientes {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCliente;
    @Column(name = "nombre")
    private String nombre;
<<<<<<< HEAD
    @Column(name = "nro_telefono")
    private String nroTelefono;
    @Column(name = "correo")
    private String correo;
    @Column(name = "ruc")
    private String ruc;
=======
    
    @Column(name = "nro_telefono")
    private String nroTelefono;

    @Column(name = "correo")
    private String correo;

    @Column(name = "ruc")
    private String ruc;

//    @OneToMany(mappedBy = "cliente", fetch = FetchType.LAZY)
//    private Set<Equipos> equipos = new HashSet<>();

>>>>>>> parent of dca61a3 (se elimino backend)
    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;
}
