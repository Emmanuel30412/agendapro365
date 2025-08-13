package com.agendapro365.backend.service;

import com.agendapro365.backend.dto.UsuarioDTO;
import com.agendapro365.backend.exception.ResourceNotFoundException;
import com.agendapro365.backend.model.Rol;
import com.agendapro365.backend.model.Usuario;
import com.agendapro365.backend.repository.UsuarioRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, BCryptPasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UsuarioDTO crearUsuario(UsuarioDTO dto) {
        System.out.println("Busca usuario con email: "+dto.getEmail());
        if (usuarioRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email ya registrado");
        }

        Usuario usuario = toEntity(dto);
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        
        usuario.setRol(Rol.valueOf(dto.getRol()));

        Usuario guardado = usuarioRepository.save(usuario);
        return toDTO(guardado);
    }

    public UsuarioDTO obtenerPorEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con email: " + email));
        return toDTO(usuario);
    }

    // Mapeo de Entity a DTO
    public UsuarioDTO toDTO(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setNombre(usuario.getNombre());
        dto.setEmail(usuario.getEmail());
        dto.setRol(usuario.getRol().name());
        return dto;
    }

    // Mapeo de DTO a Entity
    public Usuario toEntity(UsuarioDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setEmail(dto.getEmail());
        usuario.setPassword(dto.getPassword());

        if (dto.getRol() == null ){
            throw new IllegalArgumentException("El rol es obligatorio");
        }
        usuario.setRol(Rol.valueOf(dto.getRol()));
        return usuario;
    }
}
