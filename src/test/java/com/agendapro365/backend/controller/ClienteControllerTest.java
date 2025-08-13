package com.agendapro365.backend.controller;

import com.agendapro365.backend.dto.ClienteDTO;
import com.agendapro365.backend.service.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ClienteControllerTest {

    // No es real
    @Mock
    private ClienteService clienteService;

    // Crea una instancia real
    @InjectMocks
    private ClienteController clienteController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCrearCliente() {
        ClienteDTO dto = new ClienteDTO();
        dto.setNombre("Juan Pérez");
        dto.setEmail("juan@example.com");

        when(clienteService.crearCliente(any(ClienteDTO.class))).thenReturn(dto);

        ResponseEntity<ClienteDTO> response = clienteController.crearCliente(dto);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Juan Pérez", response.getBody().getNombre());
        verify(clienteService, times(1)).crearCliente(any(ClienteDTO.class));
    }

    @Test
    void testObtenerClientePorId() {
        ClienteDTO dto = new ClienteDTO();
        dto.setNombre("Ana Gómez");
        dto.setEmail("ana@example.com");

        when(clienteService.obtenerClientePorId(1L)).thenReturn(dto);

        ResponseEntity<ClienteDTO> response = clienteController.obtenerClientePorId(1L);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Ana Gómez", response.getBody().getNombre());
        verify(clienteService, times(1)).obtenerClientePorId(1L);
    }

    @Test
    void testListarClientes() {
        ClienteDTO dto1 = new ClienteDTO();
        dto1.setNombre("Juan Pérez");
        ClienteDTO dto2 = new ClienteDTO();
        dto2.setNombre("Ana Gómez");

        when(clienteService.listarClientes()).thenReturn(List.of(dto1, dto2));

        ResponseEntity<List<ClienteDTO>> response = clienteController.listarClientes();

        assertEquals(200, response.getStatusCode().value());
        assertEquals(2, response.getBody().size());
        verify(clienteService, times(1)).listarClientes();
    }

    @Test
    void testActualizarCliente() {
        ClienteDTO dto = new ClienteDTO();
        dto.setNombre("Juan Actualizado");

        when(clienteService.actualizarClienteDTO(eq(1L), any(ClienteDTO.class))).thenReturn(dto);

        ResponseEntity<ClienteDTO> response = clienteController.actualizarCliente(1L, dto);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Juan Actualizado", response.getBody().getNombre());
        verify(clienteService, times(1)).actualizarClienteDTO(eq(1L), any(ClienteDTO.class));
    }

    @Test
    void testEliminarCliente() {
        doNothing().when(clienteService).eliminarCliente(1L);

        ResponseEntity<Void> response = clienteController.eliminarCliente(1L);

        assertEquals(204, response.getStatusCode().value());
        verify(clienteService, times(1)).eliminarCliente(1L);
    }
}
