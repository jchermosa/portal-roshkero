package com.backend.portalroshkabackend.Services.UsuariosService;


import com.backend.portalroshkabackend.DTO.UsuarioDTO.UserDto;
import com.backend.portalroshkabackend.DTO.UsuarioDTO.UserHomeDto;
import com.backend.portalroshkabackend.DTO.UsuarioDTO.UserUpdateDto;
import com.backend.portalroshkabackend.Models.AsignacionUsuario;
import com.backend.portalroshkabackend.Models.Equipos;
import com.backend.portalroshkabackend.Models.Usuario;
import com.backend.portalroshkabackend.Repositories.UserRepository;
import com.backend.portalroshkabackend.Repositories.UsuarioRepositories.AsignacionUsuarioRepository;

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

    @Autowired
    private AsignacionUsuarioRepository asignacionUsuarioRepository;

    

    public List<Usuario> getUsuario(){
        return (List<Usuario>) UsuarioRepository.findAll();
    }

    public Optional<Usuario> getUsuario(Integer id){
        return UsuarioRepository.findById(id);
    }

    public void saveUsuario(Usuario Usuario){
        UsuarioRepository.save(Usuario);
    }

    public void delete(Integer id){
        UsuarioRepository.deleteById(id);
    }
    
    public Usuario getUserByCorreo(String correo) {
        Optional<Usuario> usuario = UsuarioRepository.findByCorreo(correo);
        return usuario.orElse(null);
    }

    public UserHomeDto getUsuarioHome() {
        // Obtener el correo del usuario autenticado del 
        /*
         * Spring Security identifica al usuario logueado mediante el contexto de seguridad. 
         * Cuando un usuario inicia sesión correctamente (por ejemplo, usando JWT o sesión),
         * Spring Security almacena la información del usuario autenticado en el objeto 
         * SecurityContextHolder
         */
        System.out.println(" Se ejecuto la funcion getUsuarioHome()");

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String correo;
        if (principal instanceof UserDetails) {
            correo = ((UserDetails) principal).getUsername();
        } else {
            correo = principal.toString();
        }

        System.out.println("Objeto principal: " + principal);
        // System.out.println("Correo del usuario autenticado: " + correo);

        Usuario usuario = getUserByCorreo(correo);
        if (usuario == null) {
            return null;
        }

        // Obtener asignaciones del usuario
        List<AsignacionUsuario> asignaciones = asignacionUsuarioRepository.findByIdUsuario(usuario);

        // Obtener equipos de las asignaciones
        List<Equipos> equipos = asignaciones.stream()
            .map(AsignacionUsuario::getEquipos)
            .distinct()
            .toList();

        System.out.println("\n \n Usuario encontrado: \n\n" + usuario);
        System.out.println("\n \n Equipos encontrados: \n\n" + equipos);

            UserHomeDto dto = mapUsuarioToDtoHome(usuario);
            dto.setEquipos(equipos); // Agrega un campo List<Equipos> en tu UserDto

            return dto;
    }

    public UserDto getUsuarioActual() {
        // Obtener el correo del usuario autenticado del 
        /*
         * Spring Security identifica al usuario logueado mediante el contexto de seguridad. 
         * Cuando un usuario inicia sesión correctamente (por ejemplo, usando JWT o sesión),
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

        System.out.println("\n \n Usuario encontrado: \n\n" + usuario);


        return mapUsuarioToDto(usuario);
    }

    public UserUpdateDto updateUsuarioActual(UserUpdateDto updateDto) {
        // Obtener el correo del usuario autenticado
        /*
         * Spring Security identifica al usuario logueado mediante el contexto de seguridad. 
         * Cuando un usuario inicia sesión correctamente (por ejemplo, usando JWT o sesión),
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

        System.out.println("Objeto principal: " + principal);
        System.out.println("Correo del usuario autenticado: " + correo);

        // Actualizar solo los campos permitidos para edición de un usuario
        if (updateDto.getNombre() != null) usuario.setNombre(updateDto.getNombre());
        if (updateDto.getApellido() != null) usuario.setApellido(updateDto.getApellido());
        if (updateDto.getTelefono() != null) usuario.setTelefono(updateDto.getTelefono());
        if (updateDto.getFechaNacimiento() != null) usuario.setFechaNacimiento(updateDto.getFechaNacimiento());
        // Agrega aquí otros campos segun futuros requerimientos

        UsuarioRepository.save(usuario);

        return mapUsuarioToDtoUpdate(usuario);
    }

    private UserHomeDto mapUsuarioToDtoHome(Usuario usuario) {
        if (usuario == null) return null;
        UserHomeDto dto = new UserHomeDto();
        dto.setIdUsuario(usuario.getIdUsuario());
        dto.setNombre(usuario.getNombre());
        dto.setApellido(usuario.getApellido());
        dto.setNroCedula(usuario.getNroCedula());
        dto.setCorreo(usuario.getCorreo());
        dto.setRol(usuario.getIdRol());
        dto.setCargo(usuario.getIdCargo());
        // dto.setEquipo(usuario.get()); //EQUIPOS A IMPLEMENTAR
        // dto.setIdRol(usuario.getIdRol() != null ? usuario.getIdRol().getIdRol() : null);
        dto.setFechaIngreso(usuario.getFechaIngreso());
        dto.setAntiguedad(usuario.getAntiguedad());
        dto.setDiasVacaciones(usuario.getDiasVacaciones());
        dto.setEstado(usuario.getEstado());
        dto.setContrasena(usuario.getContrasena());
        dto.setTelefono(usuario.getTelefono());
        // dto.setIdEquipo(null); // Campo no disponible en el modelo Usuario actual
        // dto.setIdCargo(usuario.getIdCargo() != null ? usuario.getIdCargo().getIdCargo() : null);
        dto.setFechaNacimiento(usuario.getFechaNacimiento());
        dto.setDiasVacacionesRestante(usuario.getDiasVacacionesRestante());
        dto.setRequiereCambioContrasena(usuario.isRequiereCambioContrasena());
        return dto;
    }

    private UserDto mapUsuarioToDto(Usuario usuario) {
        if (usuario == null) return null;
        UserDto dto = new UserDto();
        dto.setIdUsuario(usuario.getIdUsuario());
        dto.setNombre(usuario.getNombre());
        dto.setApellido(usuario.getApellido());
        dto.setNroCedula(usuario.getNroCedula());
        dto.setCorreo(usuario.getCorreo());
        dto.setRol(usuario.getIdRol());
        dto.setIdRol(usuario.getIdRol() != null ? usuario.getIdRol().getIdRol() : null);
        dto.setFechaIngreso(usuario.getFechaIngreso());
        dto.setAntiguedad(usuario.getAntiguedad());
        dto.setDiasVacaciones(usuario.getDiasVacaciones());
        dto.setEstado(usuario.getEstado());
        dto.setContrasena(usuario.getContrasena());
        dto.setTelefono(usuario.getTelefono());
        // dto.setIdEquipo(null); // Campo no disponible en el modelo Usuario actual
        dto.setIdCargo(usuario.getIdCargo() != null ? usuario.getIdCargo().getIdCargo() : null);
        dto.setFechaNacimiento(usuario.getFechaNacimiento());
        dto.setDiasVacacionesRestante(usuario.getDiasVacacionesRestante());
        dto.setRequiereCambioContrasena(usuario.isRequiereCambioContrasena());
        return dto;
    }

    private UserUpdateDto mapUsuarioToDtoUpdate(Usuario usuario) {
        if (usuario == null) return null;
        UserUpdateDto dto = new UserUpdateDto();
        // dto.setIdUsuario(usuario.getIdUsuario());
        dto.setNombre(usuario.getNombre());
        dto.setApellido(usuario.getApellido());
        dto.setNroCedula(usuario.getNroCedula());
        dto.setCorreo(usuario.getCorreo());
        // dto.setRol(usuario.getIdRol());
        dto.setIdRol(usuario.getIdRol() != null ? usuario.getIdRol().getIdRol() : null);
        dto.setFechaIngreso(usuario.getFechaIngreso());
        // dto.setAntiguedad(usuario.getAntiguedad());
        // dto.setDiasVacaciones(usuario.getDiasVacaciones());
        dto.setEstado(usuario.getEstado());
        // dto.setContrasena(usuario.getContrasena());
        dto.setTelefono(usuario.getTelefono());
        // dto.setIdEquipo(null); // Campo no disponible en el modelo Usuario actual
        dto.setIdCargo(usuario.getIdCargo() != null ? usuario.getIdCargo().getIdCargo() : null);
        dto.setFechaNacimiento(usuario.getFechaNacimiento());
        // dto.setDiasVacacionesRestante(usuario.getDiasVacacionesRestante());
        dto.setRequiereCambioContrasena(usuario.isRequiereCambioContrasena());
        return dto;
    }

}
