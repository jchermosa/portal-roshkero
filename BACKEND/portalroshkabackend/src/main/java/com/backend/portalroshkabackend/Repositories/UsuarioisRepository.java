package com.backend.portalroshkabackend.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.backend.portalroshkabackend.Models.Usuario;

public interface UsuarioisRepository extends JpaRepository<Usuario, Integer> {

}
