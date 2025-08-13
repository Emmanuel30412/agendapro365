package com.agendapro365.backend.service;

import com.agendapro365.backend.dto.ClienteDTO;
import com.agendapro365.backend.exception.ResourceNotFoundException;
import com.agendapro365.backend.model.Cliente;
import com.agendapro365.backend.model.Usuario;
import com.agendapro365.backend.repository.ClienteRepository;
import com.agendapro365.backend.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private ClienteService clienteService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCrearCliente_Success() {
        ClienteDTO dto = new ClienteDTO();
        dto.setNombre("Juan");
        dto.setEmail("juan@example.com");
        dto.setTelefono("12345678");
        dto.setUsuarioId(1L);

        Usuario usuario = new Usuario();
        usuario.setId(1L);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(clienteRepository.save(any(Cliente.class))).thenAnswer(invocation -> {
            Cliente c = invocation.getArgument(0);
            c.setId(1L);
            return c;
        });

        // Llamamos la funcion que queremos probar
        ClienteDTO resultado = clienteService.crearCliente(dto);

        // Verificamos que el resultado no sea null (Que haya un resultado)
        assertNotNull(resultado);

        // Verificamos que el id del cliente creado sea 1
        assertEquals(1L, resultado.getId());

        // Verificamos que el nombre del cliente creado sea igual al que enviamos
        assertEquals(dto.getNombre(), resultado.getNombre());

        // Verificamos que el metodo save se haya llamado exactamente una vez
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    public void testObtenerClientePorId_Success() {
        Long id = 1L;

        Cliente cliente = new Cliente();
        cliente.setId(id);
        cliente.setNombre("Juan");
        cliente.setEmail("juan@example.com");
        cliente.setTelefono("12345678");
        
        when(clienteRepository.findById(id)).thenReturn(Optional.of(cliente));

        ClienteDTO resultado = clienteService.obtenerClientePorId(id);
        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
        assertEquals("Juan", resultado.getNombre());
        verify(clienteRepository, times(1)).findById(id);
    }

    @Test
    public void testObtenerClientePorId_NotFound() {
        Long id =  1L;

        when(clienteRepository.findById(id)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            clienteService.obtenerClientePorId(id);
        });

        assertEquals("Cliente no encontrado", exception.getMessage());
        verify(clienteRepository, timeout(1)).findById(id);
    }
}
