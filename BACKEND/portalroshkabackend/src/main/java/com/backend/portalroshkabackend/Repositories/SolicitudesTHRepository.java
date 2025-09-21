package com.backend.portalroshkabackend.Repositories;

import com.backend.portalroshkabackend.DTO.th.self.MisSolicitudesResponseDto;
import com.backend.portalroshkabackend.Models.Enum.EstadoSolicitudEnum;
import com.backend.portalroshkabackend.Models.EstadoSolicitud;
import com.backend.portalroshkabackend.Models.SolicitudLideres;
import com.backend.portalroshkabackend.Models.SolicitudesTH;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SolicitudesTHRepository extends JpaRepository<SolicitudesTH, Integer> {

    @Query("""
   SELECT s
   FROM SolicitudLideres sl
   JOIN sl.solicitudesTH s
   WHERE sl.estado = :estadoLider
   AND s.estado = :estadoTh
   """)
    Page<SolicitudesTH> findAllByEstadoLiderAndEstadoTh(
            @Param("estadoLider") EstadoSolicitudEnum estadoLider,
            @Param("estadoTh") EstadoSolicitudEnum estadoTh,
            Pageable pageable);

    Page<SolicitudesTH> findAllByEstado(EstadoSolicitudEnum estado, Pageable pageable);
    Page<SolicitudesTH> findAllByUsuario_idUsuario(int idUsuario, Pageable pageable);

    Boolean existsByUsuario_idUsuarioAndEstado(int idUsuario, EstadoSolicitudEnum estado);

    SolicitudesTH findByUsuario_IdUsuarioAndIdSolicitudTH(int idUsuario, int idSolicitudTh);

}
