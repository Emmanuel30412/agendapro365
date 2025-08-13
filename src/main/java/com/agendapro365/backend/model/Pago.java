package com.agendapro365.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "pagos")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Monto es obligatorio")
    private BigDecimal monto;

    private String metodoPago; // Ejemplo: Tarjeta, PayPal, etc.

    private LocalDateTime fechaPago;

    @ManyToOne
    @JoinColumn(name = "cita_id")
    private Cita cita;

    // Getters y setters
}
