package com.agendapro365.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Entity
@Table(name = "clientes")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nombre es obligatorio")
    private String nombre;

    @Email(message = "Email inválido")
    @Column(unique = true)
    private String email;

    private String telefono;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario; // Relaciona al cliente con el usuario que lo creó o maneja

    // Getters y setters
}
