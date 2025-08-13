package com.agendapro365.backend.controller;

import com.agendapro365.backend.dto.UsuarioDTO;
import com.agendapro365.backend.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//Va a testear solo el controlador
@WebMvcTest(UsuarioController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UsuarioControllerTest {

    //MockMvc: es un Objeto que simula peticiones HTTP a los endpoint
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    //convierte objeto de java a JASON
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testRegistrarUsuario() throws Exception {

        //Crear DTO con datos de prueba
        UsuarioDTO dto = new UsuarioDTO();
        dto.setNombre("Emmanuel");
        dto.setEmail("emmanuel@example.com");
        dto.setPassword("password123");
        dto.setRol("CLIENTE");

        // Configurar el mock del servicio para que retorne el DTO cuando se llame crearUsuario
        when(usuarioService.crearUsuario(any(UsuarioDTO.class))).thenReturn(dto);

        // Simular petición POST a /api/usuarios/registro enviando el DTO como JSON
        mockMvc.perform(post("/api/usuarios/registro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())//espera codigo 200 ok
                .andExpect(jsonPath("$.email").value(dto.getEmail()));// Espera que el JSON de respuesta tenga el email esperado
    }

    @Test
    public void testObtenerUsuarioPorEmail() throws Exception {
        String email = "emmanuel@example.com";
        UsuarioDTO dto = new UsuarioDTO();
        dto.setEmail(email);
        dto.setNombre("Emmanuel");
        dto.setRol("CLIENTE");

        when(usuarioService.obtenerPorEmail(email)).thenReturn(dto);

        // Simula una petición HTTP GET al endpoint /api/usuarios/{email}
        // Perform: Ejecuta una peticion HTTP
        // Expect: verifica la respuesta
        mockMvc.perform(get("/api/usuarios/{email}", email)
            .with(user("testuser").roles("CLIENTE"))  // aquí el rol va con 'roles(...)'
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.email").value(email));
    }
}
