package com.backend.portalroshkabackend.Repositories;


import com.backend.portalroshkabackend.Models.Enum.EstadoActivoInactivo;
import com.backend.portalroshkabackend.Models.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Usuario, Integer> {


    Page<Usuario> findAllByEstado(EstadoActivoInactivo estado, Pageable pageable); //Busca todos los usuarios activos
    Page<Usuario> findAllByOrderByRolAsc(Pageable pageable); // Retona los usuarios ordenados por rol// Retona los usuarios ordenados por equipo
    Page<Usuario> findAllByOrderByCargoAsc(Pageable pageable); // Retona los usuarios ordenados por cargo

    boolean existsByCorreo(String correo);
    boolean existsByNroCedula(String nroCedula);
    boolean existsByTelefono(String telefono);
    boolean existsByCorreoAndIdUsuarioNot(String correo, Integer idUsuario); // Si el correo / cedula ya existe excluyendo al propio usuario
    boolean existsByNroCedulaAndIdUsuarioNot(String nroCedula, Integer idUsuario);
    boolean existsByTelefonoAndIdUsuarioNot(String telefono, Integer idUsuario);

    Optional<Usuario> findByNroCedula(String nroCedula);

    Optional<Usuario> findByCorreo(String correo);
}
