package com.backend.portalroshkabackend.Repositories;


import com.backend.portalroshkabackend.Models.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Usuario, Integer> {

    @Query(value =  "SELECT * FROM usuarios WHERE estado = true", nativeQuery = true)
    Page<Usuario> findAllActiveEmployees(Pageable pageable); //Busca todos los usuarios activos

    @Query(value = "SELECT * FROM usuarios WHERE estado = false", nativeQuery = true)
    Page<Usuario> findAllInactiveEmployees(Pageable pageable); // Busca todos los usuarios inactivos

    Page<Usuario> findAllByOrderByIdRolAsc(Pageable pageable); // Retona los usuarios ordenados por rol
    // Page<Usuario> findAllByOrderByIdEquipoAsc(Pageable pageable); // Retona los usuarios ordenados por equipo
    Page<Usuario> findAllByOrderByIdCargoAsc(Pageable pageable); // Retona los usuarios ordenados por cargo

     Optional<Usuario> findByNroCedula(Integer nroCedula);

    Optional<Usuario> findByCorreo(String correo);
}
