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
        config.setAllowedMethods(java.util.List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        config.setAllowCredentials(true);
        config.setAllowedHeaders(java.util.List.of("Authorization","Content-Type","Accept","Origin","X-Requested-With"));
        
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
