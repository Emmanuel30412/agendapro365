package com.agendapro365.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.StackWalker.Option;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.agendapro365.backend.dto.CitaDTO;
import com.agendapro365.backend.exception.ResourceNotFoundException;
import com.agendapro365.backend.model.Cita;
import com.agendapro365.backend.model.Cliente;
import com.agendapro365.backend.model.EstadoCita;
import com.agendapro365.backend.model.Profesional;
import com.agendapro365.backend.repository.CitaRepository;
import com.agendapro365.backend.repository.ClienteRepository;
import com.agendapro365.backend.repository.ProfesionalRepository;

@ExtendWith(MockitoExtension.class)
public class CitaServiceTest {

    @Mock
    private CitaRepository citaRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock 
    private ProfesionalRepository profesionalRepository;

    @InjectMocks
    private CitaService citaService;

    private Cita cita;
    private CitaDTO citaDTO;
    private Cliente cliente;
    private Profesional profesional;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        cliente.setId(1L);

        profesional = new Profesional();
        profesional.setId(2L);

        cita = new Cita();
        cita.setId(1L);
        cita.setFechaHora(LocalDateTime.now());
        cita.setEstado(EstadoCita.PENDIENTE);
        cita.setCliente(cliente);
        cita.setProfesional(profesional);

        citaDTO = new CitaDTO();
        citaDTO.setFechaHora(LocalDateTime.now());
        citaDTO.setEstado(EstadoCita.PENDIENTE.name());
        citaDTO.setClienteId(1L);
        citaDTO.setProfesionalId(2L);
    }
    
    @Test
    void crearCita_exitoso() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(profesionalRepository.findById(2L)).thenReturn(Optional.of(profesional));
        when(citaRepository.save(any(Cita.class))).thenReturn(cita);

        CitaDTO result = citaService.crearCita(citaDTO);

        assertNotNull(result);
        assertEquals(EstadoCita.PENDIENTE.name(), result.getEstado());
        verify(citaRepository, times(1)).save(any(Cita.class));
    }

    @Test
    void obtenerPorId_cuandoExiste() {
        when(citaRepository.findById(1L)).thenReturn(Optional.of(cita));

        CitaDTO result = citaService.obtenerPorId(1L);

        assertEquals(EstadoCita.PENDIENTE.name(), result.getEstado());
    }

    @Test
    void obtenerPorId_cuandoNoExiste() {
        when(citaRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, ()-> citaService.obtenerPorId(1L));
    }

    @Test
    void actualizarCita_exitoso() {
        when(citaRepository.findById(1L)).thenReturn(Optional.of(cita));
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(profesionalRepository.findById(1L)).thenReturn(Optional.of(profesional));
        when(citaRepository.save(any(Cita.class))).thenReturn(cita);

        CitaDTO result = citaService.actualizarCita(1L, citaDTO);
        assertNotNull(result);
        verify(citaRepository, times(1)).save(any(Cita.class));
    }

    @Test
    void eliminarCita_exitoso() {
        when(citaRepository.findById(1L)).thenReturn(Optional.of(cita));
        doNothing().when(citaRepository).delete(cita);

        citaService.eliminarCita(1L);

        verify(citaRepository, times(1)).delete(cita);
    }
}
