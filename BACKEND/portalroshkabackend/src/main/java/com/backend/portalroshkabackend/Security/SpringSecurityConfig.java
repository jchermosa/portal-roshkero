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
import com.backend.portalroshkabackend.Services.UserService;

@Configuration
public class SpringSecurityConfig {

    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    @Autowired
    private UserService userService; // ✅ inyectamos UserService

    // Bean de AuthenticationManager
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // Bean de PasswordEncoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        System.out.println("Default password encoded: " + new BCryptPasswordEncoder().encode("bbb"));
        return new BCryptPasswordEncoder(); 
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
                .requestMatchers(HttpMethod.GET, "api/v1/usuarios/**").permitAll()
                .requestMatchers(HttpMethod.GET,"api/v1/admin/operaciones").hasRole("4") // Solo usuarios con rol 4 pueden acceder
                .requestMatchers(HttpMethod.POST,"api/v1/admin/operaciones").hasRole("4") // Solo usuarios con rol 4 pueden acceder
                .requestMatchers(HttpMethod.DELETE,"api/v1/admin/operaciones").hasRole("4") // Solo usuarios con rol 4 pueden acceder
                .requestMatchers(HttpMethod.PUT,"api/v1/admin/operaciones").hasRole("4") // Solo usuarios con rol 4 pueden acceder
                .requestMatchers(HttpMethod.GET,"api/v1/admin/th").hasRole("1") 
                .requestMatchers(HttpMethod.POST,"api/v1/admin/th").hasRole("1") 
                .requestMatchers(HttpMethod.DELETE,"api/v1/admin/th").hasRole("1") 
                .requestMatchers(HttpMethod.PUT,"api/v1/admin/th").hasRole("1") 
                // .requestMatchers(HttpMethod.GET,"api/v1/usuarios").permitAll() 
                .anyRequest().authenticated()
            )
            .addFilter(jwtAuthenticationFilter) // Authentication filter for login
            .addFilter(new JwtValidationFilter(authenticationManager())) // Validation filter for all other requests
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session ->
                session.sessionCreationPolicy(org.springframework.security.config.http.SessionCreationPolicy.STATELESS)
            );

        return http.build();
    }
}
