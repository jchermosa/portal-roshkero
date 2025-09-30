package com.backend.portalroshkabackend.Repositories.TH;

import com.backend.portalroshkabackend.Models.Enum.EstadoActivoInactivo;
import com.backend.portalroshkabackend.Models.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Usuario, Integer> {

    List<Usuario> findAllByCargo_IdCargo(Integer idCargo);;// Por Vladimir

    @Query("SELECT u FROM Usuario u WHERE u.rol.idRol = 4  AND u.disponibilidad > 0")
    List<Usuario> findAllUsuariosByRol4();

    Page<Usuario> findAllByEstado(EstadoActivoInactivo estado, Pageable pageable); // Busca todos los usuarios activos

    Page<Usuario> findAllByOrderByRolAsc(Pageable pageable); // Retona los usuarios ordenados por rol// Retona los
                                                             // usuarios ordenados por equipo

    Page<Usuario> findAllByOrderByCargoAsc(Pageable pageable); // Retona los usuarios ordenados por cargo

    boolean existsByCorreo(String correo);

    boolean existsByNroCedula(String nroCedula);

    boolean existsByTelefono(String telefono);

    boolean existsByCorreoAndIdUsuarioNot(String correo, Integer idUsuario); // Si el correo / cedula ya existe
                                                                             // excluyendo al propio usuario

    boolean existsByNroCedulaAndIdUsuarioNot(String nroCedula, Integer idUsuario);

    boolean existsByTelefonoAndIdUsuarioNot(String telefono, Integer idUsuario);

    boolean existsByCargo_IdCargo(int idCargo);

    boolean existsByRol_IdRol(int idRol);

    Optional<Usuario> findByNroCedula(String nroCedula);

    Optional<Usuario> findByCorreo(String correo);

    List<Usuario> findByIdUsuarioNotIn(List<Integer> list);//

    List<Usuario> findAllByCargo_IdCargo(int idCargo);

    List<Usuario> findAllByRol_IdRol(int idRol);
}
