package com.agendapro365.backend.dto;

/**
 * DTO que contiene el token JWT generado luego de una autenticación exitosa.
 * Este token será enviado al cliente para que lo utilice en futuras solicitudes.
 */
public class JwtResponse {
    private String token;

    public JwtResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
