package com.backend.portalroshkabackend.Services.TeamLeaderServicio;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.backend.portalroshkabackend.DTO.TeamLeaderDTO.SolTeamLeaderDTO;
import com.backend.portalroshkabackend.DTO.TeamLeaderDTO.SolicitudRespuestaDto;
import com.backend.portalroshkabackend.Models.PermisosAsignados;
import com.backend.portalroshkabackend.Models.Solicitud;
import com.backend.portalroshkabackend.Models.TipoPermisos;
import com.backend.portalroshkabackend.Models.Usuario;
import com.backend.portalroshkabackend.Models.VacacionesAsignadas;
import com.backend.portalroshkabackend.Models.Enum.EstadoSolicitudEnum;
import com.backend.portalroshkabackend.Models.Enum.SolicitudesEnum;
import com.backend.portalroshkabackend.Repositories.UsuarioRepositories.AsigPermSoliRepository;
import com.backend.portalroshkabackend.Repositories.UsuarioRepositories.SolicitudesTHRepository;
import com.backend.portalroshkabackend.Repositories.UsuarioRepositories.TipoPermisosRepository;
import com.backend.portalroshkabackend.Repositories.UsuarioRepositories.UsuarioRepository;
import com.backend.portalroshkabackend.Repositories.UsuarioRepositories.VacacAsignaRepository;

@Service
public class TeamLeaderService {
    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    private SolicitudesTHRepository solicitudesTHRepository;

    @Autowired
    private TipoPermisosRepository tipoPermisosRepository;

    @Autowired
    private AsigPermSoliRepository asigPermSoliRepository;

    @Autowired
    private VacacAsignaRepository vacacAsignaRepository;

    // @Autowired
    // private 

    public Usuario getUserByCorreo(String correo) {
        Optional<Usuario> usuario = usuarioRepository.findByCorreo(correo);
        return usuario.orElse(null);
    }

    public Solicitud getSolicitudById(int idSolicitud) {
        Optional<Solicitud> solicitud = solicitudesTHRepository.findById(idSolicitud);
        return solicitud.orElse(null);
    }

    public TipoPermisos getTipoPermisoById(int idTipoPermiso) {
        Optional<TipoPermisos> tipoPermiso = tipoPermisosRepository.findById(idTipoPermiso);
        // Lógica para obtener el TipoPermisos por su ID
        return tipoPermiso.orElse(null); 
    }

    public List<SolTeamLeaderDTO> getSolicitudesLiderActual() {
    
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String correo;
        if (principal instanceof UserDetails) {
            correo = ((UserDetails) principal).getUsername();
        } else {
            correo = principal.toString();
        }

        Usuario usuario = getUserByCorreo(correo);
        if (usuario == null) {
            return null;
        }

        // Obtener las solicitudes donde el usuario actual es el líder
        List<Solicitud> solicitudes = solicitudesTHRepository.findByLider(usuario);

        //TODO: Mostrar solo las solicitudes que esten en estado "P" (Pendiente)

        // Convertir la lista de Solicitud a SolTeamLeaderDTO
        return solicitudes.stream().map(solicitud -> {
            SolTeamLeaderDTO dto = new SolTeamLeaderDTO();
            dto.setIdSolicitud(solicitud.getIdSolicitud());
            dto.setIdUsuario(solicitud.getUsuario().getIdUsuario());
            dto.setTipoSolicitud(solicitud.getTipoSolicitud());

            if (solicitud.getComentario() != null) {
                dto.setComentario(limpiarComentario(solicitud.getComentario()));
            } else {
                dto.setComentario(solicitud.getComentario());
            }
            dto.setEstado(solicitud.getEstado());
            dto.setFechaInicio(solicitud.getFechaInicio());
            dto.setCantDias(solicitud.getCantDias());
            dto.setFechaFin(solicitud.getFechaFin());
            dto.setFechaCreacion(solicitud.getFechaCreacion());
            return dto;
        }).toList();
    }

    public SolicitudRespuestaDto acceptRequest(int idSolicitud) {

        //Optional<Solicitud> solicitud = solicitudesTHRepository.findById(idSolicitud);

        Solicitud solicitud = getSolicitudById(idSolicitud);

        if (solicitud == null) {
            throw new RuntimeException("Solicitud no encontrada");
        }

        switch (solicitud.getTipoSolicitud()) {
            case SolicitudesEnum.VACACIONES -> acceptSolicitudVacaciones(solicitud);
            case SolicitudesEnum.PERMISO -> acceptSolicitudPermiso(solicitud);
            default -> throw new IllegalArgumentException("Enum no manejado");
        }
        
        SolicitudRespuestaDto respuesta = new SolicitudRespuestaDto();
        respuesta.setIdSolicitud(idSolicitud);
        respuesta.setMessage( solicitud.getTipoSolicitud() + " aceptada correctamente");

        
        return respuesta; // Placeholder response
    }

    public void acceptSolicitudVacaciones(Solicitud solicitud) {
        // Lógica para aceptar la solicitud de vacaciones
        solicitud.setEstado(EstadoSolicitudEnum.A); // Cambiar el estado a "Aprobada"
    
        VacacionesAsignadas vacacionesAsignadas = new VacacionesAsignadas();
        
        vacacionesAsignadas.setSolicitud(solicitud);
        vacacionesAsignadas.setDiasUtilizados(solicitud.getCantDias());
        vacacionesAsignadas.setConfirmacionTH(false);
        vacacionesAsignadas.setFechaCreacion(java.time.LocalDateTime.now());

        Usuario usuario = solicitud.getUsuario();

        // usuario.setDiasDisponibles(usuario.getDiasDisponibles() - solicitud.getCantDias());
        usuario.setDiasVacacionesRestante(usuario.getDiasVacacionesRestante() - solicitud.getCantDias());

        usuarioRepository.save(usuario);
        solicitudesTHRepository.save(solicitud);
        vacacAsignaRepository.save(vacacionesAsignadas);


    }

    public void acceptSolicitudPermiso(Solicitud solicitud) {
        // Lógica para aceptar la solicitud de permiso

        solicitud.setEstado(EstadoSolicitudEnum.A);; // Cambiar el estado a "Aprobada"

        PermisosAsignados permisosAsignados = new PermisosAsignados();

        String comentario = solicitud.getComentario();

        Integer idTipoPermiso = extraerIdTipoPermiso(comentario);

        if (idTipoPermiso == null)
            throw new IllegalArgumentException("No se pudo extraer el tipo de permiso del comentario.");

        TipoPermisos tipoPermisos = getTipoPermisoById(idTipoPermiso);
        if (tipoPermisos == null)
            throw new IllegalArgumentException("Tipo de permiso no encontrado con ID: " + idTipoPermiso);
        
        permisosAsignados.setTipoPermiso(tipoPermisos);
        permisosAsignados.setSolicitud(solicitud);
        permisosAsignados.setConfirmacionTH(false);


        asigPermSoliRepository.save(permisosAsignados);
        solicitudesTHRepository.save(solicitud);
    }

    private Integer extraerIdTipoPermiso(String comentario) {
        Pattern pattern = Pattern.compile("\\((\\d+)\\)");
        Matcher matcher = pattern.matcher(comentario);

        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        }
        return null;
    }

    public String limpiarComentario(String comentario) {
        // Elimina cualquier grupo como (123) o {456789} al inicio del texto
        return comentario.replaceAll("^(\\s*\\([^)]*\\)\\s*)?(\\s*\\{[^}]*\\}\\s*)?", "").trim();
    }


    public SolicitudRespuestaDto rejectRequest(int idSolicitud) {

        //Optional<Solicitud> solicitud = solicitudesTHRepository.findById(idSolicitud);

        Solicitud solicitud = getSolicitudById(idSolicitud);

        if (solicitud == null) {
            throw new RuntimeException("Solicitud no encontrada");
        }

        solicitud.setEstado(EstadoSolicitudEnum.R); // Setea la solicitud como rechazada
        
        SolicitudRespuestaDto respuesta = new SolicitudRespuestaDto();
        respuesta.setIdSolicitud(idSolicitud);
        respuesta.setMessage("Solicitud rechazada correctamente");

        solicitudesTHRepository.save(solicitud);

        return respuesta; // Placeholder response
    }




}
