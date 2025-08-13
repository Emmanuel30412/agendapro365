package com.agendapro365.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "citas")
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Fecha y hora son obligatorias")
    private LocalDateTime fechaHora;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "profesional_id")
    private Profesional profesional;

    @Enumerated(EnumType.STRING)
    private EstadoCita estado;

    // Getters y setters
}
