package com.backend.portalroshkabackend.Repositories.UsuarioRepositories;

import com.backend.portalroshkabackend.Models.AsignacionUsuario;
import com.backend.portalroshkabackend.Models.Usuario;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AsignacionUsuarioRepository extends JpaRepository<AsignacionUsuario, Integer> {
    List<AsignacionUsuario> findByIdUsuario(Usuario usuario);
}