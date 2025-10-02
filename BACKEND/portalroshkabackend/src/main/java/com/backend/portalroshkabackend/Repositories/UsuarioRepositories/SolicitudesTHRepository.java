package com.backend.portalroshkabackend.Repositories.UsuarioRepositories;

import com.backend.portalroshkabackend.Models.Solicitud;
import com.backend.portalroshkabackend.Models.Usuario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolicitudesTHRepository extends JpaRepository<Solicitud, Integer> {
    List<Solicitud> findByUsuario(Usuario usuario);
    List<Solicitud> findByLider(Usuario lider);
}

//Repositorio de las Solicitudes de los usuarios
