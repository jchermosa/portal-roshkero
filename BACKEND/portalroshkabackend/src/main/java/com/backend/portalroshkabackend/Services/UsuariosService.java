package com.backend.portalroshkabackend.Services;

<<<<<<< HEAD
import com.backend.portalroshkabackend.DTO.UserDto;
import com.backend.portalroshkabackend.DTO.UserUpdateDto;
import com.backend.portalroshkabackend.Exceptions.ResourceNotFoundException;
import com.backend.portalroshkabackend.Models.Cargos;
import com.backend.portalroshkabackend.Models.Roles;
import com.backend.portalroshkabackend.Models.Usuario;
import com.backend.portalroshkabackend.Models.Enum.EstadoActivoInactivo;
import com.backend.portalroshkabackend.Repositories.CargosRepository;
import com.backend.portalroshkabackend.Repositories.RolesRepository;
import com.backend.portalroshkabackend.Repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuariosService {
    
    private final UserRepository userRepository;
    private final RolesRepository rolesRepository;
    private final CargosRepository cargosRepository;

    @Autowired
    public UsuariosService(UserRepository userRepository, RolesRepository rolesRepository,
            CargosRepository cargosRepository) {
        this.userRepository = userRepository;
        this.rolesRepository = rolesRepository;
        this.cargosRepository = cargosRepository;
    }

    @Transactional(readOnly = true)
    public List<Usuario> getAllUsuarios() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<UserDto> getAllUsuariosDto() {
        List<Usuario> usuarios = userRepository.findAll();
        return usuarios.stream().map(this::mapUsuarioToDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<UserDto> getAllUsuarios(Pageable pageable) {
        Page<Usuario> usuarios = userRepository.findAll(pageable);
        return usuarios.map(this::mapUsuarioToDto);
    }

    @Transactional(readOnly = true)
    public Optional<Usuario> getUsuario(Integer id) {
        return userRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public UserDto getUsuarioDto(Integer id) {
        Usuario usuario = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));
        return mapUsuarioToDto(usuario);
    }

    @Transactional
    public Usuario saveUsuario(Usuario usuario) {
        return userRepository.save(usuario);
    }

    @Transactional
    public UserDto createUsuario(UserDto userDto) {
        // Validar datos únicos
        if (userRepository.existsByCorreo(userDto.getCorreo())) {
            throw new IllegalArgumentException("El correo ya está registrado");
        }
        if (userRepository.existsByNroCedula(userDto.getNroCedula())) {
            throw new IllegalArgumentException("El número de cédula ya está registrado");
        }
        if (userDto.getTelefono() != null && userRepository.existsByTelefono(userDto.getTelefono())) {
            throw new IllegalArgumentException("El número de teléfono ya está registrado");
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(userDto.getNombre());
        usuario.setApellido(userDto.getApellido());
        usuario.setNroCedula(userDto.getNroCedula());
        usuario.setCorreo(userDto.getCorreo());
        usuario.setRoles(userDto.getRoles());
        usuario.setFechaIngreso(userDto.getFechaIngreso());
        usuario.setEstado(userDto.getEstado() != null ? userDto.getEstado() : EstadoActivoInactivo.A);
        usuario.setContrasena(userDto.getContrasena());
        usuario.setTelefono(userDto.getTelefono());
        usuario.setCargos(userDto.getCargos());
        if (userDto.getFechaNacimiento() != null) {
            usuario.setFechaNacimiento(LocalDate.parse(userDto.getFechaNacimiento().toString()));
        }
        usuario.setDiasVacacionesRestante(userDto.getDiasVacacionesRestante());
        usuario.setRequiereCambioContrasena(userDto.isRequiereCambioContrasena());
        usuario.setUrl(userDto.getUrl());
        usuario.setDisponibilidad(userDto.getDisponibilidad());

        Usuario savedUsuario = userRepository.save(usuario);
        return mapUsuarioToDto(savedUsuario);
    }

    @Transactional
    public void delete(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuario no encontrado con id: " + id);
        }
        userRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public Usuario getUserByCorreo(String correo) {
        Optional<Usuario> usuario = userRepository.findByCorreo(correo);
        return usuario.orElse(null);
    }

    @Transactional(readOnly = true)
    public UserDto getUserDtoByCorreo(String correo) {
        Usuario usuario = userRepository.findByCorreo(correo)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con correo: " + correo));
        return mapUsuarioToDto(usuario);
    }

    @Transactional(readOnly = true)
    public Page<UserDto> getUsersByEstado(EstadoActivoInactivo estado, Pageable pageable) {
        Page<Usuario> usuarios = userRepository.findAllByEstado(estado, pageable);
        return usuarios.map(this::mapUsuarioToDto);
    }

    @Transactional
    public UserDto getUsuarioActual() {
        // Obtener el correo del usuario autenticado
=======
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

>>>>>>> parent of dca61a3 (se elimino backend)
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String correo;
        if (principal instanceof UserDetails) {
            correo = ((UserDetails) principal).getUsername();
        } else {
            correo = principal.toString();
        }

<<<<<<< HEAD
=======
        System.out.println("Objeto principal: " + principal);
        System.out.println("Correo del usuario autenticado: " + correo);

>>>>>>> parent of dca61a3 (se elimino backend)
        Usuario usuario = getUserByCorreo(correo);
        if (usuario == null) {
            return null;
        }

        return mapUsuarioToDto(usuario);
    }

<<<<<<< HEAD
    @Transactional
    public UserDto updateUsuarioActual(UserUpdateDto updateDto) {
        // Obtener el correo del usuario autenticado
=======
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
>>>>>>> parent of dca61a3 (se elimino backend)
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
<<<<<<< HEAD
        if (updateDto.getNombre() != null) usuario.setNombre(updateDto.getNombre());
        if (updateDto.getApellido() != null) usuario.setApellido(updateDto.getApellido());
        if (updateDto.getTelefono() != null) usuario.setTelefono(updateDto.getTelefono());
        if (updateDto.getFechaNacimiento() != null) {
            usuario.setFechaNacimiento(LocalDate.parse(updateDto.getFechaNacimiento().toString()));
        }

        Usuario updatedUsuario = userRepository.save(usuario);
        return mapUsuarioToDto(updatedUsuario);
    }

    @Transactional
    public UserDto updateUsuario(Integer id, UserDto userDto) {
        Usuario existingUsuario = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));

        // Validar datos únicos
        if (userDto.getCorreo() != null && !userDto.getCorreo().equals(existingUsuario.getCorreo())
                && userRepository.existsByCorreoAndIdUsuarioNot(userDto.getCorreo(), id)) {
            throw new IllegalArgumentException("El correo ya está registrado");
        }
        if (userDto.getNroCedula() != 0 && userDto.getNroCedula() != existingUsuario.getNroCedula()
                && userRepository.existsByNroCedulaAndIdUsuarioNot(userDto.getNroCedula(), id)) {
            throw new IllegalArgumentException("El número de cédula ya está registrado");
        }
        if (userDto.getTelefono() != null && !userDto.getTelefono().equals(existingUsuario.getTelefono())
                && userRepository.existsByTelefonoAndIdUsuarioNot(userDto.getTelefono(), id)) {
            throw new IllegalArgumentException("El número de teléfono ya está registrado");
        }

        // Actualizar campos
        if (userDto.getNombre() != null) existingUsuario.setNombre(userDto.getNombre());
        if (userDto.getApellido() != null) existingUsuario.setApellido(userDto.getApellido());
        if (userDto.getNroCedula() != 0) existingUsuario.setNroCedula(userDto.getNroCedula());
        if (userDto.getCorreo() != null) existingUsuario.setCorreo(userDto.getCorreo());
        if (userDto.getRoles() != null) existingUsuario.setRoles(userDto.getRoles());
        if (userDto.getFechaIngreso() != null) existingUsuario.setFechaIngreso(userDto.getFechaIngreso());
        if (userDto.getEstado() != null) existingUsuario.setEstado(userDto.getEstado());
        if (userDto.getContrasena() != null) existingUsuario.setContrasena(userDto.getContrasena());
        if (userDto.getTelefono() != null) existingUsuario.setTelefono(userDto.getTelefono());
        if (userDto.getCargos() != null) existingUsuario.setCargos(userDto.getCargos());
        if (userDto.getFechaNacimiento() != null) {
            existingUsuario.setFechaNacimiento(LocalDate.parse(userDto.getFechaNacimiento().toString()));
        }
        if (userDto.getDiasVacacionesRestante() != 0) existingUsuario.setDiasVacacionesRestante(userDto.getDiasVacacionesRestante());
        existingUsuario.setRequiereCambioContrasena(userDto.isRequiereCambioContrasena());
        if (userDto.getUrl() != null) existingUsuario.setUrl(userDto.getUrl());
        if (userDto.getDisponibilidad() != null) existingUsuario.setDisponibilidad(userDto.getDisponibilidad());

        Usuario updatedUsuario = userRepository.save(existingUsuario);
        return mapUsuarioToDto(updatedUsuario);
    }

    @Transactional
    public UserDto changeUserStatus(Integer id, EstadoActivoInactivo estado) {
        Usuario usuario = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));
        usuario.setEstado(estado);
        Usuario updatedUsuario = userRepository.save(usuario);
        return mapUsuarioToDto(updatedUsuario);
    }

    private UserDto mapUsuarioToDto(Usuario usuario) {
        if (usuario == null) return null;
        UserDto dto = new UserDto();
        dto.setIdUsuario(usuario.getIdUsuario());
        dto.setNombre(usuario.getNombre());
        dto.setApellido(usuario.getApellido());
        dto.setNroCedula(usuario.getNroCedula());
        dto.setCorreo(usuario.getCorreo());
        dto.setRoles(usuario.getRoles());
        dto.setFechaIngreso(usuario.getFechaIngreso());
        dto.setAntiguedad(usuario.getAntiguedad());
        dto.setDiasVacaciones(usuario.getDiasVacaciones());
        dto.setEstado(usuario.getEstado());
        dto.setContrasena(usuario.getContrasena()); // Considerar no devolver la contraseña por seguridad
        dto.setTelefono(usuario.getTelefono());
        dto.setCargos(usuario.getCargos());
        if (usuario.getFechaNacimiento() != null) {
            dto.setFechaNacimiento(java.sql.Date.valueOf(usuario.getFechaNacimiento()));
        }
        dto.setDiasVacacionesRestante(usuario.getDiasVacacionesRestante());
        dto.setRequiereCambioContrasena(usuario.isRequiereCambioContrasena());
        dto.setUrl(usuario.getUrl());
        dto.setDisponibilidad(usuario.getDisponibilidad());
        return dto;
    }
=======
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

>>>>>>> parent of dca61a3 (se elimino backend)
}
