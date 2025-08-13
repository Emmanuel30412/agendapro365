package com.agendapro365.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Set;

@Data
@Entity
@Table(name = "profesionales")
public class Profesional {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nombre es obligatorio")
    private String nombre;

    private String especialidad;

    private String email;

    // Un profesional tiene varias citas
    @OneToMany(mappedBy = "profesional", cascade = CascadeType.ALL)
    private Set<Cita> citas;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario; // El usuario que administra este profesional
}

