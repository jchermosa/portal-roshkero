package com.backend.portalroshkabackend.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.backend.portalroshkabackend.Models.Usuario;

public interface UsuarioisRepository extends JpaRepository<Usuario, Integer> {

        // If the property is named "rol" and has an "id" field:
    @Query("SELECT u FROM Usuario u WHERE u.rol.idRol = 4 AND u.idUsuario NOT IN " +
       "(SELECT a.usuario.idUsuario FROM AsignacionUsuarioEquipo a WHERE a.equipo.idEquipo = :idEquipo)")
    List<Usuario> findUsuariosNoEnEquipo(@Param("idEquipo") Integer idEquipo);
}
