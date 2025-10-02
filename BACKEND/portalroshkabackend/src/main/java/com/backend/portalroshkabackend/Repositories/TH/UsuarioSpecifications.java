package com.backend.portalroshkabackend.Repositories.TH;

import com.backend.portalroshkabackend.Models.Enum.EstadoActivoInactivo;
import com.backend.portalroshkabackend.Models.Usuario;
import org.springframework.data.jpa.domain.Specification;

public class UsuarioSpecifications {
    public static Specification<Usuario> hasRol(Integer rolId) {
        return (root, query, cb) ->
                rolId == null ? null : cb.equal(root.get("rol").get("idRol"), rolId);
    }

    public static Specification<Usuario> hasCargo(Integer cargoId) {
        return (root, query, cb) ->
                cargoId == null ? null : cb.equal(root.get("cargo").get("idCargo"), cargoId);
    }

    public static Specification<Usuario> hasEstado(EstadoActivoInactivo estado) {
        return (root, query, cb) ->
                estado == null ? null : cb.equal(root.get("estado"), estado);
    }
}
