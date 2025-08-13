package com.agendapro365.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProfesionalDTO {

    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    private String especialidad;

    private String email;

    private Long usuarioId;

    // Getters y setters
}
