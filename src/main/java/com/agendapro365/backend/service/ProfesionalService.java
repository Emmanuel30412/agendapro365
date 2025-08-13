package com.agendapro365.backend.service;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.agendapro365.backend.dto.ProfesionalDTO;
import com.agendapro365.backend.exception.ResourceNotFoundException;
import com.agendapro365.backend.model.Profesional;
import com.agendapro365.backend.model.Usuario;
import com.agendapro365.backend.repository.ProfesionalRepository;
import com.agendapro365.backend.repository.UsuarioRepository;

@Service
public class ProfesionalService {

    private final ProfesionalRepository profesionalRepository;
    private final UsuarioRepository usuarioRepository;

    public ProfesionalService(ProfesionalRepository profesionalRepository, UsuarioRepository usuarioRepository) {
        this.profesionalRepository = profesionalRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public ProfesionalDTO crearProfesional(ProfesionalDTO dto) {
        Profesional profesional = toEntity(dto);
        Profesional guardado = profesionalRepository.save(profesional);
        return toDTO(guardado);
    }

    public List<ProfesionalDTO> obtenerTodos() {
        List<Profesional> lista = profesionalRepository.findAll();
        return lista.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public ProfesionalDTO obtenerPorId(Long id) {
        Profesional profesional = profesionalRepository.findById(id)
               .orElseThrow(()-> new ResourceNotFoundException("Profesional no encontrado"));
            return toDTO(profesional);
    }

    public ProfesionalDTO actualizarProfesional(Long id, ProfesionalDTO dto) {
        Profesional existente = profesionalRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Profesional no encontrado"));

        existente.setNombre(dto.getNombre());
        existente.setEmail(dto.getEmail());
        existente.setEspecialidad(dto.getEspecialidad());

        Profesional actualizado = profesionalRepository.save(existente);
        return toDTO(actualizado);
    }

    public void eliminarProfesional(Long id) {
        Profesional profesional = profesionalRepository.findById(id)
           .orElseThrow(() -> new ResourceNotFoundException("Profesional no encontrado"));
        profesionalRepository.delete(profesional);
    }

    private ProfesionalDTO toDTO(Profesional profesional) {
        ProfesionalDTO dto = new ProfesionalDTO();
        dto.setId(profesional.getId());
        dto.setId(profesional.getId());
        dto.setNombre(profesional.getNombre());
        dto.setEmail(profesional.getEmail());
        dto.setEspecialidad(profesional.getEspecialidad());

        if (profesional.getUsuario() != null) {
            dto.setUsuarioId(profesional.getUsuario().getId());
        }
        return dto;
    }

    private Profesional toEntity(ProfesionalDTO dto) {
        Profesional p = new Profesional();
        p.setNombre(dto.getNombre());
        p.setEmail(dto.getEmail());
        p.setEspecialidad(dto.getEspecialidad());

        if (dto.getUsuarioId() != null) {
            Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                  .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
                 p.setUsuario(usuario);
        }
        return p;
    }
}
