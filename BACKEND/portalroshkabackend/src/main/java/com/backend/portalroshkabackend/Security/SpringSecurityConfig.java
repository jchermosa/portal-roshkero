 package com.backend.portalroshkabackend.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
<<<<<<< HEAD
=======

>>>>>>> parent of dca61a3 (se elimino backend)
import com.backend.portalroshkabackend.Security.filter.JwtAuthenticationFilter;
import com.backend.portalroshkabackend.Security.filter.JwtValidationFilter;
import com.backend.portalroshkabackend.Services.UsuariosService;

@Configuration
public class SpringSecurityConfig {

    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    @Autowired
    private UsuariosService userService; // ✅ inyectamos UserService

    // Bean de AuthenticationManager
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // Bean de PasswordEncoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); 
    }
    
    // Configuración de CORS
    @Bean
    public org.springframework.web.cors.CorsConfigurationSource corsConfigurationSource() {
        org.springframework.web.cors.CorsConfiguration config = new org.springframework.web.cors.CorsConfiguration();
        config.setAllowedOrigins(java.util.List.of("http://localhost:5173")); // Origen del frontend
<<<<<<< HEAD
        config.setAllowedMethods(java.util.List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowCredentials(true);
        config.setAllowedHeaders(java.util.List.of("Authorization", "Content-Type"));
=======
        config.setAllowedMethods(java.util.List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        config.setAllowCredentials(true);
        config.setAllowedHeaders(java.util.List.of("Authorization","Content-Type","Accept","Origin","X-Requested-With"));
>>>>>>> parent of dca61a3 (se elimino backend)
        
        org.springframework.web.cors.UrlBasedCorsConfigurationSource source = 
            new org.springframework.web.cors.UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    // Configuración de SecurityFilterChain
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // Creamos la instancia de JwtAuthenticationFilter pasando AuthenticationManager y UserService
        JwtAuthenticationFilter jwtAuthenticationFilter =
                new JwtAuthenticationFilter(authenticationManager(), userService);

        // Configuramos la URL de login
        jwtAuthenticationFilter.setFilterProcessesUrl("/login");

        http
            .authorizeHttpRequests(auth -> auth
<<<<<<< HEAD
                //.requestMatchers(HttpMethod.POST, "").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/usuarios/**").permitAll()
                .requestMatchers(HttpMethod.GET,"/api/v1/admin/operations/**").hasAuthority("ROLE_4") 
                .requestMatchers(HttpMethod.POST,"/api/v1/admin/operations/**").hasAuthority("ROLE_4") 
                .requestMatchers(HttpMethod.DELETE,"/api/v1/admin/operations/**").hasAuthority("ROLE_4") 
                .requestMatchers(HttpMethod.PUT,"/api/v1/admin/operations/**").hasAuthority("ROLE_4") 
                .requestMatchers(HttpMethod.GET,"/api/v1/admin/sysadmin/**").hasAuthority("ROLE_5") 
                .requestMatchers(HttpMethod.POST,"/api/v1/admin/sysadmin/**").hasAuthority("ROLE_5") 
                .requestMatchers(HttpMethod.DELETE,"/api/v1/admin/sysadmin/**").hasAuthority("ROLE_5") 
                .requestMatchers(HttpMethod.PUT,"/api/v1/admin/sysadmin/**").hasAuthority("ROLE_5") 
                .requestMatchers(HttpMethod.GET,"/api/v1/admin/th/**").hasAuthority("ROLE_1") 
                .requestMatchers(HttpMethod.POST,"/api/v1/admin/th/**").hasAuthority("ROLE_1") 
                .requestMatchers(HttpMethod.DELETE,"/api/v1/admin/th/**").hasAuthority("ROLE_1") 
                .requestMatchers(HttpMethod.PUT,"/api/v1/admin/th/**").hasAuthority("ROLE_1") 
                // .requestMatchers(HttpMethod.GET,"/api/v1/usuarios").permitAll() 
=======
                // Endpoints públicos primero
                .requestMatchers(HttpMethod.GET, "/api/v1/usuarios/**").permitAll()
                
                // Reglas específicas ANTES de las generales - ORDEN IMPORTANTE
                
                // ROLE_1 - TALENTO HUMANO: Acceso a recursos humanos
                .requestMatchers("/api/v1/admin/th/**").hasAnyAuthority("ROLE_1", "ROLE_5") 
                
                // ROLE_2 - OPERACIONES: Acceso a operaciones
                .requestMatchers("/api/v1/admin/operations/**").hasAnyAuthority("ROLE_2", "ROLE_5") 
                
                // ROLE_3 - ADMINISTRADOR DE SISTEMAS: Acceso a sysadmin
                .requestMatchers("/api/v1/admin/sysadmin/**").hasAnyAuthority("ROLE_3", "ROLE_5") 
                
                // ROLE_5 - DIRECTIVO: Esta regla debe ir AL FINAL porque es muy amplia
                .requestMatchers("/api/v1/admin/**").hasAnyAuthority("ROLE_5")
                
                // Cualquier otra request requiere autenticación
>>>>>>> parent of dca61a3 (se elimino backend)
                .anyRequest().authenticated()
            )
            .addFilter(jwtAuthenticationFilter) // Authentication filter for login
            .addFilter(new JwtValidationFilter(authenticationManager())) // Validation filter for all other requests
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configure(http)) // Habilitar CORS
            .sessionManagement(session ->
                session.sessionCreationPolicy(org.springframework.security.config.http.SessionCreationPolicy.STATELESS)
            );

        return http.build();
    }
}
