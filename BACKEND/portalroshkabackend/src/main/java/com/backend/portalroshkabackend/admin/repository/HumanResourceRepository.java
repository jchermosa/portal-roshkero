package com.backend.portalroshkabackend.admin.repository;


import com.backend.portalroshkabackend.common.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HumanResourceRepository extends JpaRepository<Usuario, Integer> {

    @Query(value =  "SELECT * FROM usuarios WHERE estado = false", nativeQuery = true)
    List<Usuario> findAllActiveEmployees(); // Busca todos los usuarios activos
}
