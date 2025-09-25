package com.backend.portalroshkabackend.Services.UsuarioServicio;

import com.backend.portalroshkabackend.DTO.UsuarioDTO.SolicitudUserDto;
import com.backend.portalroshkabackend.DTO.UsuarioDTO.UserDto;
import com.backend.portalroshkabackend.DTO.UsuarioDTO.UserHomeDto;
import com.backend.portalroshkabackend.DTO.UsuarioDTO.UserUpdateDto;
import com.backend.portalroshkabackend.Models.AsignacionUsuarioEquipo;
import com.backend.portalroshkabackend.Models.Equipos;
import com.backend.portalroshkabackend.Models.Solicitud;
import com.backend.portalroshkabackend.Models.Usuario;
import com.backend.portalroshkabackend.Repositories.TH.UserRepository;
import com.backend.portalroshkabackend.Repositories.UsuarioRepositories.AsigUsuarioEquipoRepository;
import com.backend.portalroshkabackend.Repositories.UsuarioRepositories.SolicitudesTHRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    UserRepository usuarioRepository;

    @Autowired
    private AsigUsuarioEquipoRepository asignacionUsuarioRepository;

    @Autowired
    private SolicitudesTHRepository solicitudesTHRepository;

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

    // Mapeo de Solicitud a SolicitudUserDto según el nuevo modelo
    private SolicitudUserDto mapSolicitudToDto(Solicitud solicitud) {
        SolicitudUserDto dto = new SolicitudUserDto();
        dto.setIdSolicitud(solicitud.getIdSolicitud());
        dto.setUsuario(solicitud.getUsuario());
        dto.setLider(solicitud.getLider());
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
