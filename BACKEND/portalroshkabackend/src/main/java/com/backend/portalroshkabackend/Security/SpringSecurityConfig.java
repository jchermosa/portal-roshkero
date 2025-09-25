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
                //.requestMatchers(HttpMethod.POST, "").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/usuarios/**").permitAll()
                .requestMatchers(HttpMethod.GET,"/api/v1/admin/operations/**").hasAuthority("ROLE_4") 
                .requestMatchers(HttpMethod.POST,"/api/v1/admin/operations/**").hasAuthority("ROLE_4") 
                .requestMatchers(HttpMethod.DELETE,"/api/v1/admin/operations/**").hasAuthority("ROLE_4") 
                .requestMatchers(HttpMethod.PUT,"/api/v1/admin/operations/**").hasAuthority("ROLE_4")
                .requestMatchers(HttpMethod.PATCH,"/api/v1/admin/operations/**").hasAuthority("ROLE_4") 
                .requestMatchers(HttpMethod.GET,"/api/v1/admin/sysadmin/**").hasAuthority("ROLE_5") 
                .requestMatchers(HttpMethod.POST,"/api/v1/admin/sysadmin/**").hasAuthority("ROLE_5") 
                .requestMatchers(HttpMethod.DELETE,"/api/v1/admin/sysadmin/**").hasAuthority("ROLE_5") 
                .requestMatchers(HttpMethod.PUT,"/api/v1/admin/sysadmin/**").hasAuthority("ROLE_5") 
                .requestMatchers(HttpMethod.GET,"/api/v1/admin/th/**").hasAuthority("ROLE_1") 
                .requestMatchers(HttpMethod.POST,"/api/v1/admin/th/**").hasAuthority("ROLE_1") 
                .requestMatchers(HttpMethod.DELETE,"/api/v1/admin/th/**").hasAuthority("ROLE_1") 
                .requestMatchers(HttpMethod.PUT,"/api/v1/admin/th/**").hasAuthority("ROLE_1") 
                // .requestMatchers(HttpMethod.GET,"/api/v1/usuarios").permitAll() 
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
