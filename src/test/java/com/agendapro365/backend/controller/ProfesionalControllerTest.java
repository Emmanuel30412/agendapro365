package com.agendapro365.backend.controller;

import com.agendapro365.backend.dto.ProfesionalDTO;
import com.agendapro365.backend.service.ProfesionalService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ProfesionalControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProfesionalService profesionalService;

    @InjectMocks
    private ProfesionalController profesionalController;

    private ObjectMapper objectMapper = new ObjectMapper();
    private ProfesionalDTO profesionalDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(profesionalController).build();

        profesionalDTO = new ProfesionalDTO();
        profesionalDTO.setId(1L);
        profesionalDTO.setNombre("Juan Pérez");
        profesionalDTO.setEmail("juan@example.com");
        profesionalDTO.setEspecialidad("Fisioterapia");
    }

    @Test
    void testCrearProfesional() throws Exception {
        when(profesionalService.crearProfesional(any(ProfesionalDTO.class))).thenReturn(profesionalDTO);

        mockMvc.perform(post("/api/profesionales")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(profesionalDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Juan Pérez"));
    }

    @Test
    void testListarProfesionales() throws Exception {
        when(profesionalService.obtenerTodos()).thenReturn(Collections.singletonList(profesionalDTO));

        mockMvc.perform(get("/api/profesionales"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Juan Pérez"));
    }

    @Test
    void testObtenerPorId() throws Exception {
        when(profesionalService.obtenerPorId(1L)).thenReturn(profesionalDTO);

        mockMvc.perform(get("/api/profesionales/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Juan Pérez"));
    }

    @Test
    void testActualizarProfesional() throws Exception {
        when(profesionalService.actualizarProfesional(eq(1L), any(ProfesionalDTO.class))).thenReturn(profesionalDTO);

        mockMvc.perform(put("/api/profesionales/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(profesionalDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Juan Pérez"));
    }

    @Test
    void testEliminarProfesional() throws Exception {
        doNothing().when(profesionalService).eliminarProfesional(1L);

        mockMvc.perform(delete("/api/profesionales/1"))
                .andExpect(status().isNoContent());
    }
}