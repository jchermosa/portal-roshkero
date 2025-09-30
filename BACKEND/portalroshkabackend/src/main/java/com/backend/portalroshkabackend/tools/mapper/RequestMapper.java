package com.backend.portalroshkabackend.tools.mapper;

import com.backend.portalroshkabackend.DTO.th.SolicitudByIdResponseDto;
import com.backend.portalroshkabackend.DTO.th.SolicitudResponseDto;
import com.backend.portalroshkabackend.DTO.th.request.RequestResponseDto;
import com.backend.portalroshkabackend.Models.Solicitud;
import com.backend.portalroshkabackend.Models.TipoBeneficios;
import com.backend.portalroshkabackend.Models.TipoPermisos;
import com.backend.portalroshkabackend.Repositories.PermisosRepository;
import com.backend.portalroshkabackend.Repositories.TH.BeneficiosRepository;
import com.backend.portalroshkabackend.tools.errors.errorslist.beneficios.BenefitTypeNotFoundException;
import com.backend.portalroshkabackend.tools.errors.errorslist.permisos.PermissionTypeNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class RequestMapper {

    @Autowired
    private  PermisosRepository permisosRepository;

    @Autowired
    private  BeneficiosRepository beneficiosRepository;


    public  RequestResponseDto toRequestResponseDto(Integer idSolicitud, String message){
        RequestResponseDto dto = new RequestResponseDto();

        dto.setId(idSolicitud);
        dto.setMessage(message);

        return dto;
    }

    public  SolicitudResponseDto toPermissionResponseDto(Solicitud solicitud){
        SolicitudResponseDto dto = new SolicitudResponseDto();

        Integer idTipoPermiso = extraerIdTipoPermiso(solicitud.getComentario());
        TipoPermisos tipoPermiso = null;

        if (idTipoPermiso != null){
            tipoPermiso = permisosRepository.findById(idTipoPermiso).orElseThrow(() -> new PermissionTypeNotFoundException(idTipoPermiso));
        }



        dto.setIdSolicitud(solicitud.getIdSolicitud());
        dto.setFechaInicio(solicitud.getFechaInicio());
        dto.setUsuario(solicitud.getUsuario().getNombre() + " " + solicitud.getUsuario().getApellido());
        dto.setCantidadDias(solicitud.getCantDias());

        if (tipoPermiso == null){
            dto.setSubTipo(null);
        } else {
            dto.setSubTipo(tipoPermiso.getNombre());
        }

        dto.setTipoSolicitud(solicitud.getTipoSolicitud().name());
        dto.setEstado(solicitud.getEstado());
        dto.setFechaCreacion(solicitud.getFechaCreacion());

        return dto;

    }

    public  SolicitudResponseDto toBenefitsResponseDto(Solicitud solicitud){
        SolicitudResponseDto dto = new SolicitudResponseDto();

        Integer idTipoBeneficio = extraerIdTipoPermiso(solicitud.getComentario());
        TipoBeneficios tipoBeneficio = null;
        if (idTipoBeneficio != null) {
            tipoBeneficio = beneficiosRepository.findById(idTipoBeneficio).orElseThrow( () -> new BenefitTypeNotFoundException(idTipoBeneficio));
        }

        dto.setIdSolicitud(solicitud.getIdSolicitud());
        dto.setFechaInicio(solicitud.getFechaInicio());
        dto.setUsuario(solicitud.getUsuario().getNombre() + " " + solicitud.getUsuario().getApellido());
        dto.setCantidadDias(solicitud.getCantDias());

        if (tipoBeneficio == null){
            dto.setSubTipo(null);
        } else {
            dto.setSubTipo(tipoBeneficio.getNombre());
        }

        dto.setTipoSolicitud(solicitud.getTipoSolicitud().name());
        dto.setEstado(solicitud.getEstado());
        dto.setFechaCreacion(solicitud.getFechaCreacion());

        return dto;

    }

    public  SolicitudResponseDto toVacationsResponseDto(Solicitud solicitud){
        SolicitudResponseDto dto = new SolicitudResponseDto();

        dto.setIdSolicitud(solicitud.getIdSolicitud());
        dto.setFechaInicio(solicitud.getFechaInicio());
        dto.setUsuario(solicitud.getUsuario().getNombre() + " " + solicitud.getUsuario().getApellido());
        dto.setCantidadDias(solicitud.getCantDias());
        dto.setTipoSolicitud(solicitud.getTipoSolicitud().name());
        dto.setSubTipo(solicitud.getTipoSolicitud().name());
        dto.setEstado(solicitud.getEstado());
        dto.setFechaCreacion(solicitud.getFechaCreacion());

        return dto;

    }


    public SolicitudByIdResponseDto toSolicitudByIdResponseDto(Solicitud solicitud) {
        SolicitudByIdResponseDto dto = new SolicitudByIdResponseDto();

        dto.setIdSolicitud(solicitud.getIdSolicitud());
        dto.setUsuario(solicitud.getUsuario().getNombre() + " " + solicitud.getUsuario().getApellido());

        if (solicitud.getDocumentoAdjunto() != null){
            dto.setDocumentoAdjunto(solicitud.getDocumentoAdjunto().getNombreArchivo());
        } else {
            dto.setDocumentoAdjunto(null);
        }

        if (solicitud.getLider() != null){
            dto.setLider(solicitud.getLider().getNombre() + " " + solicitud.getLider().getApellido());
        } else {
            dto.setLider(null);
        }

        dto.setTipoSolicitud(solicitud.getTipoSolicitud());

        if (solicitud.getComentario() != null){
            dto.setComentario(limpiarComentario(solicitud.getComentario()));
        } else {
            dto.setComentario(null);
        }

        dto.setEstado(solicitud.getEstado());
        dto.setFechaInicio(solicitud.getFechaInicio());

        if (solicitud.getCantDias() != null){
            dto.setCantDias(solicitud.getCantDias());
        } else {
            dto.setCantDias(0);
        }

        if (solicitud.getFechaFin() != null){
            dto.setFechaFin(solicitud.getFechaFin());
        } else {
            dto.setFechaFin(null);
        }

        if (solicitud.getFechaCreacion() != null){
            dto.setFechaCreacion(solicitud.getFechaCreacion());
        } else {
            dto.setFechaCreacion(null);
        }


        return dto;
    }

    private  Integer extraerIdTipoPermiso(String comentario) {
        Pattern pattern = Pattern.compile("\\((\\d+)\\)");
        Matcher matcher = pattern.matcher(comentario);

        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        return null;
    }

    private String limpiarComentario(String comentario) {
        if (comentario == null) return null;

        return comentario.replaceAll("\\(\\d+\\)", "")
                .replaceAll("\\{\\d+}", "")
                .trim();
    }

}
