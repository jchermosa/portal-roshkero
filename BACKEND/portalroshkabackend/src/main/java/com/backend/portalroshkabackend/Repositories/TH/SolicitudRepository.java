package com.backend.portalroshkabackend.Repositories.TH;

import com.backend.portalroshkabackend.Models.Enum.EstadoSolicitudEnum;
import com.backend.portalroshkabackend.Models.Enum.SolicitudesEnum;
import com.backend.portalroshkabackend.Models.Solicitud;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SolicitudRepository extends JpaRepository<Solicitud, Integer> {

//     @Query("""
//    SELECT s
//    FROM SolicitudLideres sl
//    JOIN sl.solicitud s
//    WHERE sl.estado = :estadoLider
//    AND s.estado = :estado
//    """)
    // Page<Solicitud> findAllByEstadoLiderAndEstado(
    //         @Param("estadoLider") EstadoSolicitudEnum estadoLider,
    //         @Param("estado") EstadoSolicitudEnum estado,
    //         Pageable pageable);

    Page<Solicitud> findAllByTipoSolicitud(SolicitudesEnum tipoSolicitud, Pageable pageable);
    Page<Solicitud> findAllByTipoSolicitudAndEstado(SolicitudesEnum tipoSolicitud, EstadoSolicitudEnum estado, Pageable pageable);
    Page<Solicitud> findAllByTipoSolicitudAndEstadoOrTipoSolicitudAndLiderIsNull(SolicitudesEnum tipoSolicitud, EstadoSolicitudEnum estado, SolicitudesEnum tipoSolicitudT, Pageable pageable);
    Page<Solicitud> findAllByEstado(EstadoSolicitudEnum estado, Pageable pageable);
    Page<Solicitud> findAllByUsuario_idUsuario(int idUsuario, Pageable pageable);

    Boolean existsByUsuario_idUsuarioAndEstado(int idUsuario, EstadoSolicitudEnum estado);

    Solicitud findByUsuario_IdUsuarioAndIdSolicitud(int idUsuario, int idSolicitud);

}
