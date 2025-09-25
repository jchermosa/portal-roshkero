package com.backend.portalroshkabackend.Services;

import com.backend.portalroshkabackend.DTO.common.UserDto;
import com.backend.portalroshkabackend.DTO.common.UserUpdateDto;
import com.backend.portalroshkabackend.Models.Usuario;
import com.backend.portalroshkabackend.Repositories.TH.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuariosService {
    @Autowired
    UserRepository UsuarioRepository;

    public List<Usuario> getUsuario() {
        return (List<Usuario>) UsuarioRepository.findAll();
    }

    public Optional<Usuario> getUsuario(Integer id) {
        return UsuarioRepository.findById(id);
    }

    public void saveUsuario(Usuario Usuario) {
        UsuarioRepository.save(Usuario);
    }

    public void delete(Integer id) {
        UsuarioRepository.deleteById(id);
    }

    public Usuario getUserByCorreo(String correo) {
        Optional<Usuario> usuario = UsuarioRepository.findByCorreo(correo);
        return usuario.orElse(null);
    }

    public UserDto getUsuarioActual() {
        // Obtener el correo del usuario autenticado del
        /*
         * Spring Security identifica al usuario logueado mediante el contexto de
         * seguridad.
         * Cuando un usuario inicia sesión correctamente (por ejemplo, usando JWT o
         * sesión),
         * Spring Security almacena la información del usuario autenticado en el objeto
         * SecurityContextHolder
         */
        System.out.println(" Se ejecuto la funcion getUsuarioActual()");

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String correo;
        if (principal instanceof UserDetails) {
            correo = ((UserDetails) principal).getUsername();
        } else {
            correo = principal.toString();
        }

        System.out.println("Objeto principal: " + principal);
        System.out.println("Correo del usuario autenticado: " + correo);

        Usuario usuario = getUserByCorreo(correo);
        if (usuario == null) {
            return null;
        }

        return mapUsuarioToDto(usuario);
    }

    public UserDto updateUsuarioActual(UserUpdateDto updateDto) {
        // Obtener el correo del usuario autenticado
        /*
         * Spring Security identifica al usuario logueado mediante el contexto de
         * seguridad.
         * Cuando un usuario inicia sesión correctamente (por ejemplo, usando JWT o
         * sesión),
         * Spring Security almacena la información del usuario autenticado en el objeto
         * SecurityContextHolder
         */
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
        if (updateDto.getNombre() != null)
            usuario.setNombre(updateDto.getNombre());
        if (updateDto.getApellido() != null)
            usuario.setApellido(updateDto.getApellido());
        if (updateDto.getTelefono() != null)
            usuario.setTelefono(updateDto.getTelefono());
        if (updateDto.getFechaNacimiento() != null)
            usuario.setFechaNacimiento(updateDto.getFechaNacimiento());
        // Agrega aquí otros campos segun futuros requerimientos

        UsuarioRepository.save(usuario);

        return mapUsuarioToDto(usuario);
    }

    private UserDto mapUsuarioToDto(Usuario usuario) {
    if (usuario == null) return null;
    UserDto dto = new UserDto();
    dto.setIdUsuario(usuario.getIdUsuario());
    dto.setNombre(usuario.getNombre());
    dto.setApellido(usuario.getApellido());
    dto.setNroCedula(usuario.getNroCedula());
    dto.setCorreo(usuario.getCorreo());
    //dto.setIdRol(usuario.getIdRol() != null ? usuario.getIdRol().getIdRol() : null);
    dto.setFechaIngreso(usuario.getFechaIngreso());
    dto.setAntiguedad(usuario.getAntiguedad());
    dto.setDiasVacaciones(usuario.getDiasVacaciones());
    dto.setEstado(usuario.getEstado());
    dto.setContrasena(usuario.getContrasena());
    dto.setTelefono(usuario.getTelefono());
    // dto.setIdEquipo(null); // Campo no disponible en el modelo Usuario actual
    //dto.setIdCargo(usuario.getIdCargo() != null ? usuario.getIdCargo().getIdCargo() : null);
    dto.setFechaNacimiento(usuario.getFechaNacimiento());
    dto.setDiasVacacionesRestante(usuario.getDiasVacacionesRestante());
    dto.setRequiereCambioContrasena(usuario.getRequiereCambioContrasena());
    return dto;
    }

}
