package com.backend.portalroshkabackend.Services;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.ArrayList;

import com.backend.portalroshkabackend.Models.Usuario;
<<<<<<< HEAD
import com.backend.portalroshkabackend.Repositories.UserRepository;
=======
import com.backend.portalroshkabackend.Repositories.TH.UserRepository;
>>>>>>> parent of dca61a3 (se elimino backend)
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
<<<<<<< HEAD
        Roles userRol = user.getRoles();
=======
        Roles userRol = user.getRol();
>>>>>>> parent of dca61a3 (se elimino backend)
        
        // Buscar el rol del usuario directamente
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        
        // Si el usuario tiene un rol asignado, agregar la autoridad
        if (userRol != null) {
<<<<<<< HEAD
            authorities.add(new SimpleGrantedAuthority("ROLE_" + userRol.getNombre()));
        }
        
        // Si no se encontró ningún rol, asignar una lista vacía
=======
            authorities.add(new SimpleGrantedAuthority("ROLE_" + userRol.getIdRol()));
        }
        
        // Si no se encontró ningún cargo, asignar una lista vacía
>>>>>>> parent of dca61a3 (se elimino backend)
        if (authorities.isEmpty()) {
            authorities = Collections.emptyList();
        }

        // Devolver el objeto User de Spring Security
        return new org.springframework.security.core.userdetails.User(
                user.getCorreo(),      // username
                user.getContrasena(),  // contraseña
<<<<<<< HEAD
                authorities            // roles/autoridades
=======
                authorities            // roles/autoridades basadas en cargo
>>>>>>> parent of dca61a3 (se elimino backend)
        );
    }
}
