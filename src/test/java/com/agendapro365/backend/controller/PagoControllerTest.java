package com.agendapro365.backend.controller;

import com.agendapro365.backend.dto.PagoDTO;
import com.agendapro365.backend.service.PagoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PagoController.class)
@AutoConfigureMockMvc(addFilters = false)
public class PagoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PagoService pagoService;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    public void testCrearPago() throws Exception {
        PagoDTO dto = new PagoDTO();
        dto.setId(1L);
        dto.setMonto(new BigDecimal("100.00"));
        dto.setMetodoPago("Tarjeta");
        dto.setFechaPago(LocalDateTime.now());
        dto.setCitaId(1L);

        when(pagoService.crearPago(any(PagoDTO.class))).thenReturn(dto);

        mockMvc.perform(post("/api/pagos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dto.getId()))
                .andExpect(jsonPath("$.monto").value(dto.getMonto().doubleValue()))
                .andExpect(jsonPath("$.metodoPago").value(dto.getMetodoPago()))
                .andExpect(jsonPath("$.citaId").value(dto.getCitaId()));
    }

    @Test
    public void testObtenerPagoPorId() throws Exception {
        Long id = 1L;
        PagoDTO dto = new PagoDTO();
        dto.setId(id);
        dto.setMonto(new BigDecimal("100.00"));
        dto.setMetodoPago("Tarjeta");
        dto.setFechaPago(LocalDateTime.now());
        dto.setCitaId(1L);

        when(pagoService.obtenerPorId(id)).thenReturn(dto);

        mockMvc.perform(get("/api/pagos/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dto.getId()))
                .andExpect(jsonPath("$.monto").value(dto.getMonto().doubleValue()))
                .andExpect(jsonPath("$.metodoPago").value(dto.getMetodoPago()))
                .andExpect(jsonPath("$.citaId").value(dto.getCitaId()));
    }

    @Test
    public void testListarPagos() throws Exception {
        PagoDTO dto1 = new PagoDTO();
        dto1.setId(1L);
        dto1.setMonto(new BigDecimal("100.00"));
        dto1.setMetodoPago("Tarjeta");
        dto1.setFechaPago(LocalDateTime.now());
        dto1.setCitaId(1L);

        PagoDTO dto2 = new PagoDTO();
        dto2.setId(2L);
        dto2.setMonto(new BigDecimal("200.00"));
        dto2.setMetodoPago("PayPal");
        dto2.setFechaPago(LocalDateTime.now());
        dto2.setCitaId(2L);

        when(pagoService.obtenerTodos()).thenReturn(Arrays.asList(dto1, dto2));

        mockMvc.perform(get("/api/pagos")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(dto1.getId()))
                .andExpect(jsonPath("$[1].id").value(dto2.getId()));
    }

    @Test
    public void testActualizarPago() throws Exception {
        Long id = 1L;
        PagoDTO dto = new PagoDTO();
        dto.setMonto(new BigDecimal("150.00"));
        dto.setMetodoPago("Efectivo");
        dto.setFechaPago(LocalDateTime.now());
        dto.setCitaId(1L);

        PagoDTO dtoActualizado = new PagoDTO();
        dtoActualizado.setId(id);
        dtoActualizado.setMonto(dto.getMonto());
        dtoActualizado.setMetodoPago(dto.getMetodoPago());
        dtoActualizado.setFechaPago(dto.getFechaPago());
        dtoActualizado.setCitaId(dto.getCitaId());

        when(pagoService.actualizarPago(eq(id), any(PagoDTO.class))).thenReturn(dtoActualizado);

        mockMvc.perform(put("/api/pagos/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.monto").value(dto.getMonto().doubleValue()))
                .andExpect(jsonPath("$.metodoPago").value(dto.getMetodoPago()))
                .andExpect(jsonPath("$.citaId").value(dto.getCitaId()));
    }

    @Test
    public void testEliminarPago() throws Exception {
        Long id = 1L;

        mockMvc.perform(delete("/api/pagos/{id}", id))
                .andExpect(status().isNoContent());
    }
}
