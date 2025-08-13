package com.agendapro365.backend.service;

import com.agendapro365.backend.dto.UsuarioDTO;
import com.agendapro365.backend.model.Rol;
import com.agendapro365.backend.model.Usuario;
import com.agendapro365.backend.repository.UsuarioRepository;
import com.agendapro365.backend.service.UsuarioService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    public void testCrearUsuario_Exitoso() {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setNombre("Emmanuel");
        dto.setEmail("emmanuel@example.com");
        dto.setPassword("password123");
        dto.setRol("CLIENTE");

        when(usuarioRepository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(dto.getPassword())).thenReturn("hashedPassword");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(i -> {
            Usuario u = i.getArgument(0);
            u.setId(1L);
            return u;
        });

        UsuarioDTO resultado = usuarioService.crearUsuario(dto);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(dto.getEmail(), resultado.getEmail());
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    public void testCrearUsuario_EmailExistente() {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setEmail("existe@example.com");
        dto.setRol("CLIENTE");

        // Simula que el usuario ya existe en la bd
    when(usuarioRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(new Usuario()));

        // Verifica que lanza IllegalArgumentException por email repetido
       IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
        usuarioService.crearUsuario(dto);
    });
        assertEquals("Email ya registrado", exception.getMessage());
    }
}
