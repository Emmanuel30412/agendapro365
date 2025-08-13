package com.agendapro365.backend.service;

import javax.naming.AuthenticationException;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.agendapro365.backend.security.JwtUtil;

/**
 * Servicio encargado de realizar la autenticacion de usuarios
 * y generacion de token JWT para el acceso a la API
 */
@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthService(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Realiza la autenticacion del usuario con las credenciales proporcionadas.
     * Si la autenticacion es exitosa, genera un token JWT para el usuario
     * 
     * @param email correo electronico del usuario
     * @param password contraseña del usuario
     * @return token JWT generado para el usuario autenticado
     * @throws RuntimeException si las credenciales son invalidos
     */
    public String authenticateAndGenerateToken(String email, String password) {

        try {
           authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
           return jwtUtil.generateToken(email);
        } catch (org.springframework.security.core.AuthenticationException e) {
            throw new RuntimeException("Credenciales invalidas");
        }
    }

}
