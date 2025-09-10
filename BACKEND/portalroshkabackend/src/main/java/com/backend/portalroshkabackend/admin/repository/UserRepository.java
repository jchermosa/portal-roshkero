package com.backend.portalroshkabackend.admin.repository;


import com.backend.portalroshkabackend.common.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<Usuario, Integer> {

    @Query(value =  "SELECT * FROM usuarios WHERE estado = true", nativeQuery = true)
    Page<Usuario> findAllActiveEmployees(Pageable pageable); //Busca todos los usuarios activos

    @Query(value = "SELECT * FROM usuarios WHERE estado = false", nativeQuery = true)
    Page<Usuario> findAllInactiveEmployees(Pageable pageable); // Busca todos los usuarios inactivos
}
