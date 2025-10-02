package com.backend.portalroshkabackend.Services.UsuarioServicio;

import com.backend.portalroshkabackend.DTO.UsuarioDTO.SolicitudUserDto;
import com.backend.portalroshkabackend.DTO.UsuarioDTO.UserCambContrasDto;
import com.backend.portalroshkabackend.DTO.UsuarioDTO.UserCargoDto;
import com.backend.portalroshkabackend.DTO.UsuarioDTO.UserDto;
import com.backend.portalroshkabackend.DTO.UsuarioDTO.UserHomeDto;
import com.backend.portalroshkabackend.DTO.UsuarioDTO.UserRolDto;
import com.backend.portalroshkabackend.DTO.UsuarioDTO.UserSolBeneficioDto;
import com.backend.portalroshkabackend.DTO.UsuarioDTO.UserSolDispositivoDto;
import com.backend.portalroshkabackend.DTO.UsuarioDTO.UserSolPermisoDto;
import com.backend.portalroshkabackend.DTO.UsuarioDTO.UserSolVacacionDto;
import com.backend.portalroshkabackend.DTO.UsuarioDTO.UserUpdateDto;
import com.backend.portalroshkabackend.DTO.UsuarioDTO.UserUpdateFoto;
import com.backend.portalroshkabackend.DTO.UsuarioDTO.tiposBeneficiosDto;
import com.backend.portalroshkabackend.DTO.UsuarioDTO.tiposDispositivosDto;
import com.backend.portalroshkabackend.DTO.UsuarioDTO.tiposPermisosDto;
import com.backend.portalroshkabackend.Models.AsignacionUsuarioEquipo;
import com.backend.portalroshkabackend.Models.Equipos;
import com.backend.portalroshkabackend.Models.Solicitud;
import com.backend.portalroshkabackend.Models.TipoBeneficios;
import com.backend.portalroshkabackend.Models.TipoDispositivo;
import com.backend.portalroshkabackend.Models.TipoPermisos;
import com.backend.portalroshkabackend.Models.Usuario;
import com.backend.portalroshkabackend.Models.Enum.EstadoActivoInactivo;
import com.backend.portalroshkabackend.Models.Enum.EstadoSolicitudEnum;
import com.backend.portalroshkabackend.Models.Enum.SolicitudesEnum;
import com.backend.portalroshkabackend.Repositories.UsuarioRepositories.AsigUsuarioEquipoRepository;
// import com.backend.portalroshkabackend.Repositories.UsuarioRepositories.BeneficiosAsignadosRepository;
import com.backend.portalroshkabackend.Repositories.UsuarioRepositories.SolicitudesTHRepository;
import com.backend.portalroshkabackend.Repositories.UsuarioRepositories.TipoBeneficiosRepository;
import com.backend.portalroshkabackend.Repositories.UsuarioRepositories.TipoPermisosRepository;
import com.backend.portalroshkabackend.Repositories.UsuarioRepositories.UsuarioRepository;
import com.backend.portalroshkabackend.Repositories.UsuarioRepositories.TipoDispositivosRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    private AsigUsuarioEquipoRepository asignacionUsuarioRepository;

    @Autowired
    private TipoPermisosRepository tipoPermisosRepository;

    @Autowired
    private TipoBeneficiosRepository tipoBeneficiosRepository;

    @Autowired
    private SolicitudesTHRepository solicitudesTHRepository;

    // @Autowired
    // private BeneficiosAsignadosRepository beneficiosAsignadosRepository;

    @Autowired
    private TipoDispositivosRepository tipoDispositivoRepository;

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

    public SolicitudUserDto getSolicitudById(Integer id) {
        Optional<Solicitud> solicitudOpt = solicitudesTHRepository.findById(id);
        if (solicitudOpt.isPresent()) {
            return mapSolicitudToDto(solicitudOpt.get());
        } else {
            return null; // o lanzar una excepción si prefieres
        }
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

    public List<tiposPermisosDto> getTiposPermisos() {
        List<TipoPermisos> tiposPermisos = tipoPermisosRepository.findAll();

        // Mapea cada TipoPermisos a tiposPermisosDto
        return tiposPermisos.stream()
                .map(this::mapTipoPermisosToDto)
                .collect(Collectors.toList());
    }

    private tiposPermisosDto mapTipoPermisosToDto(TipoPermisos tipoPermisos) {
        tiposPermisosDto dto = new tiposPermisosDto();
        dto.setIdTipoPermiso(tipoPermisos.getIdTipoPermiso());
        dto.setNombre(tipoPermisos.getNombre());
        dto.setCantDias(tipoPermisos.getCantDias());
        dto.setObservaciones(tipoPermisos.getObservaciones());
        dto.setRemunerado(tipoPermisos.getRemunerado());
        dto.setFuerzaMenor(tipoPermisos.getFuerzaMenor());
        return dto;
    }

    public List<tiposBeneficiosDto> getTiposBeneficios() {

        // Obtiene todos los tipos de beneficios con vigencia Activa desde el repositorio
        // List<TipoBeneficios> tiposBeneficios = tipoBeneficiosRepository.findAll();
        List<TipoBeneficios> tiposBeneficios = tipoBeneficiosRepository.findByVigencia(EstadoActivoInactivo.A);

        // Mapea cada TipoBeneficios a tiposBeneficiosDto
        return tiposBeneficios.stream()
                .map(this::mapTipoBeneficiosToDto)
                .collect(Collectors.toList());
    }

    private tiposBeneficiosDto mapTipoBeneficiosToDto(TipoBeneficios tipoBeneficios) {
        tiposBeneficiosDto dto = new tiposBeneficiosDto();
        dto.setIdTipoBeneficio(tipoBeneficios.getIdTipoBeneficio());
        dto.setNombre(tipoBeneficios.getNombre());
        dto.setDescripcion(tipoBeneficios.getDescripcion());
        dto.setMontoMaximo(tipoBeneficios.getMontoMaximo());
        return dto;
    }

    public List<tiposDispositivosDto> getTiposDispositivos() {

        // Obtiene todos los tipos de dispositivos con vigencia Activa desde el repositorio
        List<TipoDispositivo> tiposDispositivos = tipoDispositivoRepository.findAll();

        // Mapea cada TipoDispositivo a tiposDispositivosDto
        return tiposDispositivos.stream()
                .map(this::mapTipoDispositivoToDto)
                .collect(Collectors.toList());
    }

    private tiposDispositivosDto mapTipoDispositivoToDto(TipoDispositivo tipoDispositivo) {
        tiposDispositivosDto dto = new tiposDispositivosDto();
        dto.setIdTipoDispositivo(tipoDispositivo.getIdTipoDispositivo());
        dto.setNombre(tipoDispositivo.getNombre());
        dto.setDetalle(tipoDispositivo.getDetalle());;
        return dto;
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
        nuevaSolicitud.setCantDias(solBeneficioDto.getCant_dias());
        nuevaSolicitud.setComentario("(" + solBeneficioDto.getId_tipo_beneficio() + ") " + "{" + solBeneficioDto.getMonto() + "} " + solBeneficioDto.getComentario());
        nuevaSolicitud.setEstado(EstadoSolicitudEnum.P); // Estado inicial pendiente
        nuevaSolicitud.setFechaCreacion(java.time.LocalDateTime.now()); // Fecha y hora actual

        nuevaSolicitud.setLider(null); // Dirigido a Talento Humano

        System.out.println("\n \n nuevaSolicitud: \n\n" + nuevaSolicitud + "\n \n");
        solicitudesTHRepository.save(nuevaSolicitud);


        solBeneficioDto.setComentario("Solicitud creada con éxito y enviada a Talento Humano");

        return solBeneficioDto;
    }


    public UserSolVacacionDto crearVacacionUsuarioActual(UserSolVacacionDto solVacacionDto) {
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

        // TipoPermisos tipoPermiso = tipoPermisosRepository.findById(solPermisoDto.getId_tipo_permiso())
        //         .orElseThrow(() -> new RuntimeException("Tipo de permiso no encontrado"));

        Solicitud nuevaSolicitud = new Solicitud();
        nuevaSolicitud.setUsuario(usuario);
        nuevaSolicitud.setTipoSolicitud(SolicitudesEnum.VACACIONES); // Asigna el tipo de solicitud correspondiente
        // nuevaSolicitud.setLider(lider); // Asigna el líder correspondiente

        if (solVacacionDto.getFecha_inicio() == null || solVacacionDto.getFecha_fin() == null) {
            throw new IllegalArgumentException("Fecha de inicio o fin no pueden ser nulos.");
        }


        nuevaSolicitud.setFechaInicio(solVacacionDto.getFecha_inicio());
        // nuevaSolicitud.setCantDias(solVacacionDto.getCant_dias());
        // nuevaSolicitud.setComentario();
        nuevaSolicitud.setEstado(EstadoSolicitudEnum.P); // Estado inicial pendiente
        nuevaSolicitud.setFechaCreacion(java.time.LocalDateTime.now()); // Fecha y hora actual
        nuevaSolicitud.setFechaFin(solVacacionDto.getFecha_fin()); // Setea la fecha final

        //TODO: Calcular la cantidad de dias habiles laborales entre las dos fechas
        //TODO:generar una lista de feriados y excluirlos del calculo de dias habiles
        // nuevaSolicitud.setCantDias((int) ChronoUnit.DAYS.between(solVacacionDto.getFecha_inicio(), solVacacionDto.getFecha_fin()) + 1 ); // Calcula la cantidad de dias entre las dos fechas inclusivo
        
        nuevaSolicitud.setCantDias(calcularDiasHabiles(solVacacionDto.getFecha_inicio(), solVacacionDto.getFecha_fin()));


        if (nuevaSolicitud.getCantDias() > usuario.getDiasVacacionesRestante()) { // si la cantidad de dias solicitados es mayor a los dias de vacaciones restantes
            throw new IllegalArgumentException(" No tienes suficientes días de vacaciones restantes. Pides: "+ nuevaSolicitud.getCantDias() + " y tienes Días restantes: " + usuario.getDiasVacacionesRestante());
        }


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

            // Validar que haya al menos dos equipos asignados para aplicar desempate
            AsignacionUsuarioEquipo asignacionPrincipal = asignaciones.get(0);
            Equipos equipoPrincipal = asignacionPrincipal.getEquipo();
            Usuario lider;

            if (asignaciones.size() > 1) { //si tiene mas de un equipo asignado
                AsignacionUsuarioEquipo asignacionSecundaria = asignaciones.get(1);
                Equipos equipoSecundario = asignacionSecundaria.getEquipo();

                Integer porcentaje1 = asignacionPrincipal.getPorcentajeTrabajo();
                Integer porcentaje2 = asignacionSecundaria.getPorcentajeTrabajo();


                if (Objects.equals(porcentaje1, porcentaje2)) { // si tienen el mismo porcentaje de trabajo se desempata por fecha limite del equipo
                    LocalDate hoy = LocalDate.now();
                    long tiempoRestante1 = equipoPrincipal.getFechaLimite() != null ? ChronoUnit.DAYS.between(hoy, equipoPrincipal.getFechaLimite()) : -1;
                    long tiempoRestante2 = equipoSecundario.getFechaLimite() != null ? ChronoUnit.DAYS.between(hoy, equipoSecundario.getFechaLimite()) : -1;

                    lider = tiempoRestante1 >= tiempoRestante2 ? equipoPrincipal.getLider() : equipoSecundario.getLider();
                } else {
                    lider = equipoPrincipal.getLider();
                }
            } else {
                lider = equipoPrincipal.getLider(); // si solo tiene un equipo asignado
            }

            nuevaSolicitud.setLider(lider); // Asigna el líder correspondiente
            
            System.out.println("\n \n asignaciones: \n\n" + asignaciones + "\n \n");            
        }

        System.out.println("\n \n nuevaSolicitud: \n\n" + nuevaSolicitud + "\n \n");
        solicitudesTHRepository.save(nuevaSolicitud);

        solVacacionDto.setComentario("Solicitud de Vacacion creada con éxito");
        solVacacionDto.setDestinatario((nuevaSolicitud.getLider() != null ? nuevaSolicitud.getLider().getNombre() + " " + nuevaSolicitud.getLider().getApellido() : "Talento Humano"));
        solVacacionDto.setCantidadDias(nuevaSolicitud.getCantDias());

        return solVacacionDto;
    }

    public UserSolDispositivoDto pedirDispositivoUsuarioActual(UserSolDispositivoDto solDispositivoDto) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String correo;
        if (principal instanceof UserDetails) {
            correo = ((UserDetails) principal).getUsername();
        } else {
            correo = principal.toString();
        }

        Usuario usuario = getUserByCorreo(correo);

        if (usuario == null) {
            throw new RuntimeException("Usuario no encontrado");
        }

        TipoDispositivo tipoDispositivo = tipoDispositivoRepository.findById(solDispositivoDto.getId_tipo_dispositivo())
                .orElseThrow(() -> new RuntimeException("Tipo de dispositivo no encontrado")); 


        Solicitud nuevaSolicitud = new Solicitud();
        nuevaSolicitud.setUsuario(usuario);
        nuevaSolicitud.setTipoSolicitud(SolicitudesEnum.DISPOSITIVO); // Asigna el tipo de solicitud correspondiente
        nuevaSolicitud.setComentario("(" + tipoDispositivo.getIdTipoDispositivo() + ") " + solDispositivoDto.getComentario());
        nuevaSolicitud.setEstado(EstadoSolicitudEnum.P); // Estado inicial pendiente
        nuevaSolicitud.setFechaCreacion(java.time.LocalDateTime.now()); // Fecha y hora actual
        nuevaSolicitud.setFechaInicio(java.time.LocalDateTime.now().toLocalDate()); // Fecha de solicitud como fecha de inicio

        System.out.println("\n \n nuevaSolicitud: \n\n" + nuevaSolicitud + "\n \n");
        solicitudesTHRepository.save(nuevaSolicitud);

        solDispositivoDto.setComentario("Solicitud de Dispositivo creada con éxito y enviada a SysAdmin");

        return solDispositivoDto;
    }

    private Integer calcularDiasHabiles(LocalDate inicio, LocalDate fin) {
        int diasHabiles = 0;
        LocalDate fecha = inicio;
        List<LocalDate> feriados = obtenerFeriados2025();

        while (!fecha.isAfter(fin)) {// mientras la fecha no sea posterior a la fecha fin
            boolean esFeriado = feriados.contains(fecha); // Verifica si la fecha es un feriado
            boolean esDiaLaboral = fecha.getDayOfWeek().getValue() >= 1 && fecha.getDayOfWeek().getValue() <= 5; // 1 = Lunes, 5 = Viernes, 6 = Sabado, 7 = Domingo

            if (esDiaLaboral && !esFeriado) {
                diasHabiles++;
            }
            fecha = fecha.plusDays(1);// avanza al siguiente día
        }

        return diasHabiles;
    }

    public static List<LocalDate> obtenerFeriados2025() {
        return List.of(
            LocalDate.of(2025, 9, 29),  // Victoria de Boquerón
            LocalDate.of(2025, 12, 8),  // Virgen de Caacupé
            LocalDate.of(2025, 12, 25),  // Navidad

            LocalDate.of(2026, 1, 1),   // Año Nuevo
            LocalDate.of(2026, 3, 1),   // Día de los Héroes
            LocalDate.of(2026, 4, 2),   // Jueves Santo
            LocalDate.of(2026, 4, 3),   // Viernes Santo
            LocalDate.of(2026, 5, 1),   // Día del Trabajador
            LocalDate.of(2026, 5, 14),  // Independencia
            LocalDate.of(2026, 5, 15),  // Día de la Madre
            LocalDate.of(2026, 6, 12),  // Paz del Chaco
            LocalDate.of(2026, 8, 15),  // Fundación de Asunción
            LocalDate.of(2026, 9, 29),  // Victoria de Boquerón
            LocalDate.of(2026, 12, 8),  // Virgen de Caacupé
            LocalDate.of(2026, 12, 25),  // Navidad

            LocalDate.of(2027, 1, 1),   // Año Nuevo
            LocalDate.of(2027, 3, 1),   // Día de los Héroes
            LocalDate.of(2027, 3, 25),  // Jueves Santo
            LocalDate.of(2027, 3, 26),  // Viernes Santo
            LocalDate.of(2027, 5, 1),   // Día del Trabajador
            LocalDate.of(2027, 5, 14),  // Independencia
            LocalDate.of(2027, 5, 15),  // Día de la Madre
            LocalDate.of(2027, 6, 12),  // Paz del Chaco
            LocalDate.of(2027, 8, 15),  // Fundación de Asunción
            LocalDate.of(2027, 9, 29),  // Victoria de Boquerón
            LocalDate.of(2027, 12, 8),  // Virgen de Caacupé
            LocalDate.of(2027, 12, 25),  // Navidad

            LocalDate.of(2028, 1, 1),   // Año Nuevo
            LocalDate.of(2028, 3, 1),   // Día de los Héroes
            LocalDate.of(2028, 4, 13),  // Jueves Santo
            LocalDate.of(2028, 4, 14),  // Viernes Santo
            LocalDate.of(2028, 5, 1),   // Día del Trabajador
            LocalDate.of(2028, 5, 14),  // Independencia
            LocalDate.of(2028, 5, 15),  // Día de la Madre
            LocalDate.of(2028, 6, 12),  // Paz del Chaco
            LocalDate.of(2028, 8, 15),  // Fundación de Asunción
            LocalDate.of(2028, 9, 29),  // Victoria de Boquerón
            LocalDate.of(2028, 12, 8),  // Virgen de Caacupé
            LocalDate.of(2028, 12, 25)  // Navidad
        );
    }

    //servicio para actualizar contraseña del usuario actual
    public boolean actualizarContrasena(UserCambContrasDto dto) {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String correo;
        if (principal instanceof UserDetails) {
            correo = ((UserDetails) principal).getUsername();
        } else {
            correo = principal.toString();
        }

        Usuario usuario = getUserByCorreo(correo);
        if (usuario == null) {
            throw new RuntimeException("Usuario no encontrado");
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();// Instancia del codificador
        
        String nuevaContrasena = dto.getNuevaContrasena(); // Nueva contraseña en texto plano
        String contrasenaActual = dto.getContrasenaActual(); // Contraseña actual en texto plano

        if (!encoder.matches(contrasenaActual, usuario.getContrasena())) { // Verifica si la contraseña actual coincide
            return false;
        }

        usuario.setContrasena(encoder.encode(nuevaContrasena)); // Actualiza la contraseña con la nueva codificada
        usuarioRepository.save(usuario);
        return true;
    }

    public boolean actualizarFoto(UserUpdateFoto dto){

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String correo;
        if (principal instanceof UserDetails) {
            correo = ((UserDetails) principal).getUsername();
        } else {
            correo = principal.toString();
        }

        Usuario usuario = getUserByCorreo(correo);
        if (usuario == null) {
            throw new RuntimeException("Usuario no encontrado");
        }

        if (!esBase64(dto.getFoto())) { // Verifica si la cadena es una imagen en base64
            return false;
        }
        usuario.setUrlPerfil(dto.getFoto());; // Actualiza la foto con la nueva codificada
        usuarioRepository.save(usuario);
        return true;
    }

    public boolean esBase64(String texto) {
        try {
            Base64.getDecoder().decode(texto);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
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
        // dto.setComentario(solicitud.getComentario());
        //limpiar el comentario para que no muestre el id del tipo de permiso o beneficio
        if (solicitud.getComentario() != null) {
            dto.setComentario(limpiarComentario(solicitud.getComentario()));
        } else {
            dto.setComentario(null);
        }
        dto.setTipoSolicitud(solicitud.getTipoSolicitud());
        dto.setEstado(solicitud.getEstado());
        dto.setFechaCreacion(solicitud.getFechaCreacion());
        dto.setNombreLider(solicitud.getLider() != null ? solicitud.getLider().getNombre() + " " + solicitud.getLider().getApellido() : null);
        dto.setNombreUsuario(solicitud.getUsuario() != null ? solicitud.getUsuario().getNombre() + " " + solicitud.getUsuario().getApellido() : null);
        
        
        Integer idSubtipoSolicitud = extraerIdSubtipoSolicitud(solicitud.getComentario());
        
        // System.out.println("ID SUBTIPO SOLICITUD EXTRAIDO: " + idSubtipoSolicitud);
        String nombreSubtipo = null;

        if (idSubtipoSolicitud != null) {
            switch (solicitud.getTipoSolicitud()) {
                case PERMISO:
                    TipoPermisos tipoPermiso = tipoPermisosRepository.findById(idSubtipoSolicitud).orElse(null);
                    if (tipoPermiso != null) nombreSubtipo = tipoPermiso.getNombre();
                    break;
                case BENEFICIO:
                    TipoBeneficios tipoBeneficio = tipoBeneficiosRepository.findById(idSubtipoSolicitud).orElse(null);
                    if (tipoBeneficio != null) nombreSubtipo = tipoBeneficio.getNombre();
                    break;
                case DISPOSITIVO:
                    TipoDispositivo tipoDispositivo = tipoDispositivoRepository.findById(idSubtipoSolicitud).orElse(null);
                    if (tipoDispositivo != null) nombreSubtipo = tipoDispositivo.getNombre();
                    break;
            }
        }
        dto.setNombreSubTipoSolicitud(nombreSubtipo);
        
        // Puedes agregar más campos según lo que necesites exponer en el DTO
        return dto;
    }

    private Integer extraerIdSubtipoSolicitud(String comentario) {
        if (comentario == null){
            return null;
        }else{
            Pattern pattern = Pattern.compile("\\((\\d+)\\)");
            Matcher matcher = pattern.matcher(comentario);
            if (matcher.find()) {
                return Integer.parseInt(matcher.group(1));
            }else{
                return null;
            }
        }
    }
    
    public String limpiarComentario(String comentario) {
        // Elimina cualquier grupo como (123) o {456789} al inicio del texto
        return comentario.replaceAll("^(\\s*\\([^)]*\\)\\s*)?(\\s*\\{[^}]*\\}\\s*)?", "").trim();
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
        UserRolDto rolDto = new UserRolDto();
        UserCargoDto cargoDto = new UserCargoDto();

        rolDto.setIdRol(usuario.getRol() != null ? usuario.getRol().getIdRol() : null);
        rolDto.setNombre(usuario.getRol() != null ? usuario.getRol().getNombre() : null);
        cargoDto.setIdCargo(usuario.getCargo() != null ? usuario.getCargo().getIdCargo() : null);
        cargoDto.setNombre(usuario.getCargo() != null ? usuario.getCargo().getNombre() : null);

        dto.setIdUsuario(usuario.getIdUsuario());
        dto.setNombre(usuario.getNombre());
        dto.setApellido(usuario.getApellido());
        dto.setNroCedula(usuario.getNroCedula());
        dto.setCorreo(usuario.getCorreo());
        // dto.setIdRol(usuario.getRol() != null ? usuario.getRol().getIdRol() : null);
        // dto.setNombreRol(usuario.getRol() != null ? usuario.getRol().getNombre() : null);
        dto.setFechaIngreso(usuario.getFechaIngreso());
        dto.setAntiguedad(usuario.getAntiguedad());
        dto.setDiasVacaciones(usuario.getDiasVacaciones());
        dto.setEstado(usuario.getEstado());
        dto.setTelefono(usuario.getTelefono());
        // dto.setIdCargo(usuario.getCargo() != null ? usuario.getCargo().getIdCargo() : null);
        dto.setFechaNacimiento(usuario.getFechaNacimiento());
        dto.setDiasVacacionesRestante(usuario.getDiasVacacionesRestante());
        dto.setRequiereCambioContrasena(usuario.getRequiereCambioContrasena());
        dto.setSeniority(usuario.getSeniority());
        dto.setFoco(usuario.getFoco());
        dto.setUrlPerfil(usuario.getUrlPerfil());
        dto.setDisponibilidad(usuario.getDisponibilidad());

        //nuevos campos dto ROL y CARGO
        dto.setRol(rolDto);
        dto.setCargo(cargoDto);

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
