package com.agendapro365.backend.controller;

import com.agendapro365.backend.dto.ClienteDTO;
import com.agendapro365.backend.model.Rol;
import com.agendapro365.backend.model.Usuario;
import com.agendapro365.backend.repository.ProfesionalRepository;
import com.agendapro365.backend.repository.UsuarioRepository;
import com.agendapro365.backend.security.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsuarioRepository usuarioRepositorio;

    @Autowired
    private ProfesionalRepository profesionalRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    String token;

    @BeforeEach
    void setUp() {
        profesionalRepository.deleteAllInBatch();
     
        // Delete all existing users
        usuarioRepositorio.deleteAllInBatch();

        // Create a test user with all required fields
        Usuario usuario = new Usuario();
        usuario.setNombre("admin");
        usuario.setEmail("admin@example.com"); // NOT NULL column
        usuario.setPassword(passwordEncoder.encode("admin123"));
        usuario.setRol(Rol.ADMIN); // must match the role required by the endpoint

        usuarioRepositorio.save(usuario);

        // Generando token para admin
        token = "Bearer "+ jwtUtil.generateToken(usuario.getEmail());
    }

    @Test
    void testCrearCliente() throws Exception {
        // Create DTO for the new client
        ClienteDTO dto = new ClienteDTO();
        dto.setNombre("Juan Pérez");
        dto.setEmail("juan@example.com");

        // Perform POST request with HTTP Basic authentication
        mockMvc.perform(post("/api/clientes")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .with(httpBasic("admin@example.com", "admin123"))) // must match email
                .andExpect(status().isOk()); // Expect 200 OK
    }
}
