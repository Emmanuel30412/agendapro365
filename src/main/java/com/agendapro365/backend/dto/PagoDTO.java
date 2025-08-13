package com.agendapro365.backend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PagoDTO {

    private Long id;

    @NotNull(message = "El monto es obligatorio")
    private BigDecimal monto;

    private String metodoPago;

    private LocalDateTime fechaPago;

    private Long citaId;

    // Getters y setters
}
