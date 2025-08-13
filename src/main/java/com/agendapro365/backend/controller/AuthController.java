package com.agendapro365.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.agendapro365.backend.dto.JwtResponse;
import com.agendapro365.backend.model.LoginRequest;
import com.agendapro365.backend.service.AuthService;
import jakarta.validation.Valid;

/**
 * Controlador REST encargado de manejar las solicitudes de autenticacion
 * Expone un edpoint para que los usuarios puedan iniciar sesion y obtener un JWTtoken
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * EndPoint para autenticacion de usuario
     * Recibe las credenciales y responde con el token JWT si son validas.
     * @param loginRequest DTO con email y password
     * @return Token JWT envuelto en JwtResponse.
     */
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        String jwtToken = authService.authenticateAndGenerateToken(
                loginRequest.getEmail(), 
                loginRequest.getPassword());
        return ResponseEntity.ok(new JwtResponse(jwtToken));
    }

    public void malasPracticaProbarSonarCloud() {
       // Probando sonar Cloud valores sin usar
        int a = 1;
        int b = 2;
        int c = 3;
        int d = 4;
    }

}
