package com.agendapro365.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.agendapro365.backend.dto.CitaDTO;
import com.agendapro365.backend.exception.ResourceNotFoundException;
import com.agendapro365.backend.model.Cita;
import com.agendapro365.backend.model.Cliente;
import com.agendapro365.backend.model.EstadoCita;
import com.agendapro365.backend.model.Profesional;
import com.agendapro365.backend.repository.CitaRepository;
import com.agendapro365.backend.repository.ClienteRepository;
import com.agendapro365.backend.repository.ProfesionalRepository;

@Service
public class CitaService {

    private final CitaRepository citaRepository;
    private final ClienteRepository clienteRepository;
    private final ProfesionalRepository profesionalRepository;

    public CitaService(CitaRepository citaRepository,
                       ClienteRepository clienteRepository,
                       ProfesionalRepository profesionalRepository) {
        this.citaRepository = citaRepository;
        this.clienteRepository = clienteRepository;
        this.profesionalRepository = profesionalRepository;
    }

    public CitaDTO crearCita(CitaDTO citaDTO) {
        Cita cita = toEntity(citaDTO);
        Cita guardada = citaRepository.save(cita);
        return toDTO(guardada);
    }

    public List<CitaDTO> obtenerTodas() {
        return citaRepository.findAll()
                  .stream()
                  .map(this::toDTO)
                  .collect(Collectors.toList());
    }

    public CitaDTO obtenerPorId(Long id) {
        Cita cita = citaRepository.findById(id)
             .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada"));

        return toDTO(cita);
    }

    public CitaDTO actualizarCita(Long id, CitaDTO dto) {
        Cita cita = citaRepository.findById(id)
             .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada"));

        cita.setFechaHora(dto.getFechaHora());
        cita.setEstado(EstadoCita.valueOf(dto.getEstado().toUpperCase()));
        
        if (dto.getClienteId() != null) {
            Cliente cliente = clienteRepository.findById(dto.getClienteId())
               .orElseThrow(()-> new ResourceNotFoundException("Cliente no encontrado"));
            
            cita.setCliente(cliente);
        } else {
            cita.setCliente(null);
        }

        if (dto.getProfesionalId() != null) {
            Profesional profesional = profesionalRepository.findById(dto.getClienteId())
                 .orElseThrow(()-> new ResourceNotFoundException("Profesional no encontrado con id: " + dto.getProfesionalId()));
            cita.setProfesional(profesional);
        } else {
            cita.setProfesional(null);
        }
    
       return toDTO(citaRepository.save(cita));
    }

    public void eliminarCita(Long id){
        Cita cita = citaRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada"));

        citaRepository.delete(cita);
    }



    private CitaDTO toDTO(Cita cita) {
        CitaDTO dto = new CitaDTO();
        dto.setId(cita.getId());
        dto.setFechaHora(cita.getFechaHora());
        dto.setEstado(cita.getEstado().name());
        dto.setClienteId(cita.getCliente() != null ? cita.getCliente().getId() : null);
        dto.setProfesionalId(cita.getProfesional() != null ? cita.getProfesional().getId() : null);        

        return dto;
    }

    private Cita toEntity(CitaDTO dto) {
        Cita cita = new Cita();
        cita.setFechaHora(dto.getFechaHora());
        
        // Convertir un estado a un Enum
        cita.setEstado(EstadoCita.valueOf(dto.getEstado().toUpperCase()));

        if (dto.getClienteId() != null) {
            Cliente cliente = clienteRepository.findById(dto.getClienteId())
                 .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado"));

            cita.setCliente(cliente);
        }

if (dto.getProfesionalId() != null) {
            Profesional profesional = profesionalRepository.findById(dto.getProfesionalId())
                .orElseThrow(() -> new ResourceNotFoundException("Profesional no encontrado"));
            cita.setProfesional(profesional);
        }
        
        return cita;
    }
}        
