package com.agendapro365.backend.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO que representa la informacion enviada por el cliente para autenticarse.
 * Contiene el email y la contraseña del usuario
 */

@Data
public class LoginRequest {

    @Email(message = "Email invalido")
    @NotBlank(message = "El email es obligatorio")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;
}
