package com.agendapro365.backend.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.agendapro365.backend.security.JwtFilter;
import com.agendapro365.backend.security.MyUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    // Definimos la cadena de filtros de seguridad
   @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
           // Desactivamos CSRF porque nuestra API no usa cookies / sesiones
           .csrf(csrf -> csrf.disable())

           // Definimos que no usaremos sesion, porque JWT es stateless
           .sessionManagement(session -> 
               session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
           )
           
           // Definimos reglas de autorizacion
            .authorizeHttpRequests(auth -> auth

                 // Endpoints publicos
                .requestMatchers(HttpMethod.POST, "/api/usuarios/registro").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                
                // Permite acceso libre a swagger y openapi
                .requestMatchers(
                    "/v3/api-docs/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/swagger-ui/index.html",
                    "/favicon.ico"
                ).permitAll()
                
                // El resto requiere autenticacion
                .anyRequest().authenticated()
            )

            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        // Construimos y devolvemos la cadena de filtros configurada    
        return http.build();
    }
    
    /**
     * Bean para encriptar contraseña con BCrypt.
     * Este se usa cuando registramos un usuario y tambien
     * cuando Spring Security compara contraseñas al hacer login
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Bean que expone el AuthenticationManager.
     * Necesario para que nuestro AuthController pueda autenticar manualmente
     * usando email/password y luego generar el JWT
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig)
           throws Exception {
            return authConfig.getAuthenticationManager();

    } 
}