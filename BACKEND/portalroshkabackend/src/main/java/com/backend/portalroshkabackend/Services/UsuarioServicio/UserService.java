package com.backend.portalroshkabackend.Services.UsuarioServicio;

import com.backend.portalroshkabackend.DTO.UsuarioDTO.SolicitudUserDto;
import com.backend.portalroshkabackend.DTO.UsuarioDTO.UserDto;
import com.backend.portalroshkabackend.DTO.UsuarioDTO.UserHomeDto;
import com.backend.portalroshkabackend.DTO.UsuarioDTO.UserSolBeneficioDto;
import com.backend.portalroshkabackend.DTO.UsuarioDTO.UserSolPermisoDto;
import com.backend.portalroshkabackend.DTO.UsuarioDTO.UserUpdateDto;
import com.backend.portalroshkabackend.Models.AsignacionUsuarioEquipo;
// import com.backend.portalroshkabackend.Models.BeneficiosAsignados;
import com.backend.portalroshkabackend.Models.Equipos;
import com.backend.portalroshkabackend.Models.Solicitud;
// import com.backend.portalroshkabackend.Models.TipoBeneficios;
import com.backend.portalroshkabackend.Models.TipoPermisos;
import com.backend.portalroshkabackend.Models.Usuario;
import com.backend.portalroshkabackend.Models.Enum.EstadoSolicitudEnum;
import com.backend.portalroshkabackend.Models.Enum.SolicitudesEnum;
import com.backend.portalroshkabackend.Repositories.TH.UserRepository;
import com.backend.portalroshkabackend.Repositories.UsuarioRepositories.AsigUsuarioEquipoRepository;
import com.backend.portalroshkabackend.Repositories.UsuarioRepositories.UserBeneficiosAsignadosRepository;
import com.backend.portalroshkabackend.Repositories.UsuarioRepositories.SolicitudesTHRepository;
import com.backend.portalroshkabackend.Repositories.UsuarioRepositories.TipoBeneficiosRepository;
import com.backend.portalroshkabackend.Repositories.UsuarioRepositories.TipoPermisosRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    UserRepository usuarioRepository;

    @Autowired
    private AsigUsuarioEquipoRepository asignacionUsuarioRepository;

    @Autowired
    private TipoPermisosRepository tipoPermisosRepository;

    @Autowired
    private TipoBeneficiosRepository tipoBeneficiosRepository;

    @Autowired
    private SolicitudesTHRepository solicitudesTHRepository;

    @Autowired
    private UserBeneficiosAsignadosRepository beneficiosAsignadosRepository;

    public List<Usuario> getUsuario() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> getUsuario(Integer id) {
        return usuarioRepository.findById(id);
    }

    public void saveUsuario(Usuario usuario) {
        usuarioRepository.save(usuario);
    }

    public void delete(Integer id) {
        usuarioRepository.deleteById(id);
    }

    public Usuario getUserByCorreo(String correo) {
        Optional<Usuario> usuario = usuarioRepository.findByCorreo(correo);
        return usuario.orElse(null);
    }

    public UserHomeDto getUsuarioHome() {
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

        // Obtener asignaciones del usuario
        List<AsignacionUsuarioEquipo> asignaciones = asignacionUsuarioRepository.findByUsuario(usuario);

        // Obtener nombres de equipos de las asignaciones
        List<String> equipos = asignaciones.stream()
                .map(a -> a.getEquipo().getNombre())
                .distinct()
                .collect(Collectors.toList());

        UserHomeDto dto = mapUsuarioToDtoHome(usuario);
        dto.setEquipos(equipos);
        

        return dto;
    }

    public UserDto getUsuarioActual() {
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

        return mapUsuarioToDto(usuario);
    }

    public UserUpdateDto updateUsuarioActual(UserUpdateDto updateDto) {
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

        // Actualizar solo los campos permitidos para edición de un usuario
        if (updateDto.getNombre() != null) usuario.setNombre(updateDto.getNombre());
        if (updateDto.getApellido() != null) usuario.setApellido(updateDto.getApellido());
        if (updateDto.getTelefono() != null) usuario.setTelefono(updateDto.getTelefono());
        if (updateDto.getFechaNacimiento() != null) usuario.setFechaNacimiento(updateDto.getFechaNacimiento());
        if (updateDto.getUrlPerfil() != null) usuario.setUrlPerfil(updateDto.getUrlPerfil());
        if (updateDto.getSeniority() != null) usuario.setSeniority(updateDto.getSeniority());
        if (updateDto.getFoco() != null) usuario.setFoco(updateDto.getFoco());
        if (updateDto.getDisponibilidad() != null) usuario.setDisponibilidad(updateDto.getDisponibilidad());
        if (updateDto.getEstado() != null) usuario.setEstado(updateDto.getEstado());
        if (updateDto.getRequiereCambioContrasena() != null) usuario.setRequiereCambioContrasena(updateDto.getRequiereCambioContrasena());
        // Agrega aquí otros campos según futuros requerimientos

        usuarioRepository.save(usuario);

        return mapUsuarioToDtoUpdate(usuario);
    }

    public List<SolicitudUserDto> getSolicitudesUsuarioActual() {
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

        List<Solicitud> solicitudes = solicitudesTHRepository.findByUsuario(usuario);

        // Mapea cada Solicitud a SolicitudUserDto
        return solicitudes.stream()
                .map(this::mapSolicitudToDto)
                .collect(Collectors.toList());
    }

    public UserSolPermisoDto crearPermisoUsuarioActual(UserSolPermisoDto solPermisoDto) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String correo;
        if (principal instanceof UserDetails) {
            correo = ((UserDetails) principal).getUsername();
        } else {
            correo = principal.toString();
        }

        Usuario usuario = getUserByCorreo(correo);

        // Usuario lider = null;

        
        if (usuario == null) {
            throw new RuntimeException("Usuario no encontrado");
        }

        TipoPermisos tipoPermiso = tipoPermisosRepository.findById(solPermisoDto.getId_tipo_permiso())
                .orElseThrow(() -> new RuntimeException("Tipo de permiso no encontrado"));

        Solicitud nuevaSolicitud = new Solicitud();
        nuevaSolicitud.setUsuario(usuario);
        nuevaSolicitud.setTipoSolicitud(SolicitudesEnum.PERMISO); // Asigna el tipo de solicitud correspondiente
        // nuevaSolicitud.setLider(lider); // Asigna el líder correspondiente

        if (solPermisoDto.getFecha_inicio() == null || solPermisoDto.getCant_dias() == null) {
            throw new IllegalArgumentException("Fecha de inicio o cantidad de días no pueden ser nulos.");
        }

        nuevaSolicitud.setFechaInicio(solPermisoDto.getFecha_inicio());
        nuevaSolicitud.setCantDias(solPermisoDto.getCant_dias());
        nuevaSolicitud.setComentario("(" + tipoPermiso.getIdTipoPermiso() + ") " + solPermisoDto.getComentario());
        nuevaSolicitud.setEstado(EstadoSolicitudEnum.P); // Estado inicial pendiente
        nuevaSolicitud.setFechaCreacion(java.time.LocalDateTime.now()); // Fecha y hora actual
        nuevaSolicitud.setFechaFin(solPermisoDto.getFecha_inicio().plusDays(solPermisoDto.getCant_dias() - 1)); // Calcula la fecha final


        // nuevaSolicitud.setLider(null); // Dirigido a Talento Humano

    /* ######## Aqui se busca el Team Leader a quien sera dirigido la solicitud ######## */

        // Obtener la lista de equipos a los que está asignado el usuario
        List<AsignacionUsuarioEquipo> asignaciones = asignacionUsuarioRepository.findByUsuario(usuario);

        // Validar que haya al menos una asignación
        if (asignaciones == null || asignaciones.isEmpty()) {
            nuevaSolicitud.setLider(null); // Dirigido a Talento Humano
            // throw new RuntimeException("El usuario no tiene asignaciones registradas");
        }else{
            // Ordenar por porcentaje de trabajo descendente
            asignaciones.sort(
                Comparator.comparing(AsignacionUsuarioEquipo::getPorcentajeTrabajo, Comparator.nullsLast(Integer::compareTo)).reversed()
            );

            // Validar que haya al menos dos asignaciones para aplicar desempate
            AsignacionUsuarioEquipo asignacionPrincipal = asignaciones.get(0);
            Equipos equipoPrincipal = asignacionPrincipal.getEquipo();
            Usuario lider;

            if (asignaciones.size() > 1) {
                AsignacionUsuarioEquipo asignacionSecundaria = asignaciones.get(1);
                Equipos equipoSecundario = asignacionSecundaria.getEquipo();

                Integer porcentaje1 = asignacionPrincipal.getPorcentajeTrabajo();
                Integer porcentaje2 = asignacionSecundaria.getPorcentajeTrabajo();


                if (Objects.equals(porcentaje1, porcentaje2)) {
                    LocalDate hoy = LocalDate.now();
                    long tiempoRestante1 = equipoPrincipal.getFechaLimite() != null ? ChronoUnit.DAYS.between(hoy, equipoPrincipal.getFechaLimite()) : -1;
                    long tiempoRestante2 = equipoSecundario.getFechaLimite() != null ? ChronoUnit.DAYS.between(hoy, equipoSecundario.getFechaLimite()) : -1;

                    lider = tiempoRestante1 >= tiempoRestante2 ? equipoPrincipal.getLider() : equipoSecundario.getLider();
                } else {
                    lider = equipoPrincipal.getLider();
                }
            } else {
                lider = equipoPrincipal.getLider();
            }

            /* En caso de que el tipo de Permiso sea Fuerza_Menor == TRUE sera dirigido al TEAM LEADER de lo contrario Sera Dirigido a TALENTO HUMANO */
            if (tipoPermiso.getFuerzaMenor()) {
                nuevaSolicitud.setLider(lider); // Asigna el líder correspondiente
            } else {
                // solPermisoDto.setId_lider(lider.getIdUsuario()); // Dirigido al Team Leader
                nuevaSolicitud.setLider(null); // Dirigido a Talento Humano
            }  
            System.out.println("\n \n asignaciones: \n\n" + asignaciones + "\n \n");            
        }

        // nuevaSolicitud.setDocumentoAdjunto(solPermisoDto.getId_documento_adjunto());
        // Aquí puedes agregar más campos según lo que necesites


        System.out.println("\n \n nuevaSolicitud: \n\n" + nuevaSolicitud + "\n \n");
        solicitudesTHRepository.save(nuevaSolicitud);

        solPermisoDto.setComentario("Solicitud creada con éxito y enviada a " + (nuevaSolicitud.getLider() != null ? nuevaSolicitud.getLider().getNombre() + " " + nuevaSolicitud.getLider().getApellido() : "Talento Humano"));

        return solPermisoDto;
    }



    public UserSolBeneficioDto crearBeneficioUsuarioActual(UserSolBeneficioDto solBeneficioDto) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String correo;
        if (principal instanceof UserDetails) {
            correo = ((UserDetails) principal).getUsername();
        } else {
            correo = principal.toString();
        }

        Usuario usuario = getUserByCorreo(correo);

        // Usuario lider = null;

        
        if (usuario == null) {
            throw new RuntimeException("Usuario no encontrado");
        }

        // TipoBeneficios tipoBeneficio = tipoBeneficiosRepository.findById(solBeneficioDto.getId_tipo_beneficio())
        //         .orElseThrow(() -> new RuntimeException("Tipo de beneficio no encontrado"));

        Solicitud nuevaSolicitud = new Solicitud();

        // BeneficiosAsignados beneficioAsignado = new BeneficiosAsignados();

        nuevaSolicitud.setUsuario(usuario);
        nuevaSolicitud.setTipoSolicitud(SolicitudesEnum.BENEFICIO); // Asigna el tipo de solicitud correspondiente
        // nuevaSolicitud.setLider(lider); // Asigna el líder correspondiente

        
        if (solBeneficioDto.getFecha_inicio() != null && solBeneficioDto.getCant_dias() != null) { // si ambos son diferentes de null 
            nuevaSolicitud.setFechaFin(solBeneficioDto.getFecha_inicio().plusDays(solBeneficioDto.getCant_dias() - 1)); // Calcula la fecha final
        }

        nuevaSolicitud.setFechaInicio(solBeneficioDto.getFecha_inicio());
        // nuevaSolicitud.setCantDias(solBeneficioDto.getCant_dias());
        nuevaSolicitud.setComentario("(" + solBeneficioDto.getId_tipo_beneficio() + ") " + "{" + solBeneficioDto.getMonto() + "} " + solBeneficioDto.getComentario());
        nuevaSolicitud.setEstado(EstadoSolicitudEnum.P); // Estado inicial pendiente
        nuevaSolicitud.setFechaCreacion(java.time.LocalDateTime.now()); // Fecha y hora actual

        nuevaSolicitud.setLider(null); // Dirigido a Talento Humano

        // beneficioAsignado.setBeneficio(tipoBeneficio);
        // beneficioAsignado.setMontoAprobado(null);
        // beneficioAsignado.setSolicitud(nuevaSolicitud);

        // Obtener la lista de equipos a los que está asignado el usuario
        // List<AsignacionUsuarioEquipo> asignaciones = asignacionUsuarioRepository.findByUsuario(usuario);

        // nuevaSolicitud.setDocumentoAdjunto(solPermisoDto.getId_documento_adjunto());
        // Aquí puedes agregar más campos según lo que necesites


        System.out.println("\n \n nuevaSolicitud: \n\n" + nuevaSolicitud + "\n \n");
        solicitudesTHRepository.save(nuevaSolicitud);


        solBeneficioDto.setComentario("Solicitud creada con éxito y enviada a Talento Humano");

        return solBeneficioDto;
    }




    // Mapeo de Solicitud a SolicitudUserDto según el nuevo modelo
    private SolicitudUserDto mapSolicitudToDto(Solicitud solicitud) {
        SolicitudUserDto dto = new SolicitudUserDto();
        dto.setIdSolicitud(solicitud.getIdSolicitud());
        // dto.setUsuario(solicitud.getUsuario());
        dto.setIdUsuario(solicitud.getUsuario() != null ? solicitud.getUsuario().getIdUsuario() : null);
        // dto.setLider(solicitud.getLider());
        dto.setIdLider(solicitud.getLider() != null ? solicitud.getLider().getIdUsuario() : null);
        dto.setFechaInicio(solicitud.getFechaInicio());
        dto.setFechaFin(solicitud.getFechaFin());
        dto.setCantDias(solicitud.getCantDias());
        dto.setComentario(solicitud.getComentario());
        dto.setTipoSolicitud(solicitud.getTipoSolicitud());
        dto.setEstado(solicitud.getEstado());
        dto.setFechaCreacion(solicitud.getFechaCreacion());
        // Puedes agregar más campos según lo que necesites exponer en el DTO
        return dto;
    }

    // Mapeo de Usuario a UserHomeDto según el nuevo modelo
    private UserHomeDto mapUsuarioToDtoHome(Usuario usuario) {
        if (usuario == null) return null;
        UserHomeDto dto = new UserHomeDto();
        dto.setIdUsuario(usuario.getIdUsuario());
        dto.setNombre(usuario.getNombre());
        dto.setApellido(usuario.getApellido());
        dto.setNroCedula(usuario.getNroCedula());
        dto.setCorreo(usuario.getCorreo());
        dto.setTelefono(usuario.getTelefono());
        dto.setFechaIngreso(usuario.getFechaIngreso());
        dto.setFechaNacimiento(usuario.getFechaNacimiento());
        dto.setEstado(usuario.getEstado());
        dto.setRequiereCambioContrasena(usuario.getRequiereCambioContrasena());
        dto.setAntiguedad(usuario.getAntiguedad());
        dto.setDiasVacaciones(usuario.getDiasVacaciones());
        dto.setDiasVacacionesRestante(usuario.getDiasVacacionesRestante());
        dto.setIdRol(usuario.getRol() != null ? usuario.getRol().getIdRol() : null);
        dto.setNombreRol(usuario.getRol() != null ? usuario.getRol().getNombre() : null);
        dto.setIdCargo(usuario.getCargo() != null ? usuario.getCargo().getIdCargo() : null);
        dto.setNombreCargo(usuario.getCargo() != null ? usuario.getCargo().getNombre() : null);
        dto.setSeniority(usuario.getSeniority());
        dto.setFoco(usuario.getFoco());
        dto.setUrlPerfil(usuario.getUrlPerfil());
        dto.setDisponibilidad(usuario.getDisponibilidad());
        // El campo equipos se setea en getUsuarioHome()
        return dto;
    }

    // Mapeo de Usuario a UserDto según el nuevo modelo
    private UserDto mapUsuarioToDto(Usuario usuario) {
        if (usuario == null) return null;
        UserDto dto = new UserDto();
        dto.setIdUsuario(usuario.getIdUsuario());
        dto.setNombre(usuario.getNombre());
        dto.setApellido(usuario.getApellido());
        dto.setNroCedula(usuario.getNroCedula());
        dto.setCorreo(usuario.getCorreo());
        dto.setIdRol(usuario.getRol() != null ? usuario.getRol().getIdRol() : null);
        dto.setNombreRol(usuario.getRol() != null ? usuario.getRol().getNombre() : null);
        dto.setFechaIngreso(usuario.getFechaIngreso());
        dto.setAntiguedad(usuario.getAntiguedad());
        dto.setDiasVacaciones(usuario.getDiasVacaciones());
        dto.setEstado(usuario.getEstado());
        dto.setTelefono(usuario.getTelefono());
        dto.setIdCargo(usuario.getCargo() != null ? usuario.getCargo().getIdCargo() : null);
        dto.setFechaNacimiento(usuario.getFechaNacimiento());
        dto.setDiasVacacionesRestante(usuario.getDiasVacacionesRestante());
        dto.setRequiereCambioContrasena(usuario.getRequiereCambioContrasena());
        dto.setSeniority(usuario.getSeniority());
        dto.setFoco(usuario.getFoco());
        dto.setUrlPerfil(usuario.getUrlPerfil());
        dto.setDisponibilidad(usuario.getDisponibilidad());
        return dto;
    }

    // Mapeo de Usuario a UserUpdateDto según el nuevo modelo
    private UserUpdateDto mapUsuarioToDtoUpdate(Usuario usuario) {
        if (usuario == null) return null;
        UserUpdateDto dto = new UserUpdateDto();
        dto.setNombre(usuario.getNombre());
        dto.setApellido(usuario.getApellido());
        dto.setNroCedula(usuario.getNroCedula());
        dto.setCorreo(usuario.getCorreo());
        dto.setIdRol(usuario.getRol() != null ? usuario.getRol().getIdRol() : null);
        dto.setFechaIngreso(usuario.getFechaIngreso());
        dto.setEstado(usuario.getEstado());
        dto.setTelefono(usuario.getTelefono());
        dto.setIdCargo(usuario.getCargo() != null ? usuario.getCargo().getIdCargo() : null);
        dto.setFechaNacimiento(usuario.getFechaNacimiento());
        dto.setRequiereCambioContrasena(usuario.getRequiereCambioContrasena());
        dto.setSeniority(usuario.getSeniority());
        dto.setFoco(usuario.getFoco());
        dto.setUrlPerfil(usuario.getUrlPerfil());
        dto.setDisponibilidad(usuario.getDisponibilidad());
        return dto;
    }
}
