package com.backend.portalroshkabackend.Services;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.ArrayList;

import com.backend.portalroshkabackend.Models.Usuario;
import com.backend.portalroshkabackend.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.portalroshkabackend.Models.Roles;

@Service
public class JpaUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        // Buscar usuario por correo
        Optional<Usuario> optionalUser = userRepository.findByCorreo(correo);

        // Verificar si existe el usuario
        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("Usuario no encontrado con el correo: " + correo);
        }

        // Obtener el usuario del Optional
        Usuario user = optionalUser.get();
        
        // Obtener el rol del usuario directamente del objeto
        Roles userRol = user.getRoles();
        
        // Buscar el rol del usuario directamente
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        
        // Si el usuario tiene un rol asignado, agregar la autoridad
        if (userRol != null) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + userRol.getNombre()));
        }
        
        // Si no se encontró ningún rol, asignar una lista vacía
        if (authorities.isEmpty()) {
            authorities = Collections.emptyList();
        }

        // Devolver el objeto User de Spring Security
        return new org.springframework.security.core.userdetails.User(
                user.getCorreo(),      // username
                user.getContrasena(),  // contraseña
                authorities            // roles/autoridades
        );
    }
}
