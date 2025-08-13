package com.agendapro365.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.agendapro365.backend.dto.ProfesionalDTO;
import com.agendapro365.backend.exception.ResourceNotFoundException;
import com.agendapro365.backend.model.Profesional;
import com.agendapro365.backend.model.Usuario;
import com.agendapro365.backend.repository.ProfesionalRepository;
import com.agendapro365.backend.repository.UsuarioRepository;

public class ProfesionalServiceTest {

    @Mock
    private ProfesionalRepository profesionalRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private ProfesionalService profesionalService;

    private Profesional profesional;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        profesional = new Profesional();
        profesional.setId(1L);
        profesional.setNombre("Juan Perez");
        profesional.setEmail("juan@example.com");
        profesional.setEspecialidad("Fisioterapia");
    }

    @Test
    void TestCrearProfesional_asignarUsuarioYGuardar() {
        ProfesionalDTO dto = new ProfesionalDTO();
        dto.setNombre("Juan Perez");
        dto.setEmail("juan@example.com");
        dto.setEspecialidad("Ingenieria");
        dto.setUsuarioId(10L);

        Usuario usuarioMock = new Usuario();
        usuarioMock.setId(10L);
        usuarioMock.setEmail("usuario@example.com");

        Profesional profesionalGuardado = new Profesional();
        profesionalGuardado.setId(1L);
        profesionalGuardado.setNombre(dto.getNombre());
        profesionalGuardado.setEmail(dto.getEmail());
        profesionalGuardado.setEspecialidad(dto.getEspecialidad());
        profesionalGuardado.setUsuario(usuarioMock);

        // Simulamos buscar usuario en repo
        when(usuarioRepository.findById(10L)).thenReturn(Optional.of(usuarioMock));

        // Simulamos guardar profesional en repo
        when(profesionalRepository.save(any(Profesional.class))).thenReturn(profesionalGuardado);

        // Ejecutamos metodo a probar
        ProfesionalDTO resultado = profesionalService.crearProfesional(dto);

        // Verificaciones
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Juan Perez", resultado.getNombre());
        assertEquals("Ingenieria", resultado.getEspecialidad());

        // Verificamos que se llamo al repositorio usuario
        verify(usuarioRepository, times(1)).findById(10L);
        
        // Verificamos que se llamo al repo profesional guardar
        verify(profesionalRepository, times(1)).save(any(Profesional.class));

    }

    @Test
    void crearProfesional_usuarioNoEncontrado_lanzaExcepcion() {
        ProfesionalDTO dto = new ProfesionalDTO();
        dto.setNombre("Maria");
        dto.setUsuarioId(99L);

        // Simulamos que no encontramos usuario
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        // Verificamos que al llamar se lanza excepcion
        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, ()-> {
            profesionalService.crearProfesional(dto);
        });

        assertEquals("Usuario no encontrado", thrown.getMessage());
    }

    @Test
    void TestCrearProfesional() {
        when(profesionalRepository.save(any(Profesional.class))).thenReturn(profesional);

        ProfesionalDTO dto = new ProfesionalDTO();
        dto.setNombre("Juan Perez");
        dto.setEmail("juan@example.com");
        dto.setEspecialidad("Fisioterapia");

        ProfesionalDTO resultado = profesionalService.crearProfesional(dto);

        assertNotNull(resultado);
        assertEquals("Juan Perez", resultado.getNombre());
        verify(profesionalRepository, times(1)).save(any(Profesional.class));
    }

    @Test
    void testObtenerTodos() {
        when(profesionalRepository.findAll()).thenReturn(Arrays.asList(profesional));
        
        List<ProfesionalDTO> lista =  profesionalService.obtenerTodos();

        assertEquals(1, lista.size());
        verify(profesionalRepository, times(1)).findAll();
    }

    @Test
    void testObtenerPorId_Exitoso() {
        when(profesionalRepository.findById(1L)).thenReturn(Optional.of(profesional));

        ProfesionalDTO dto = profesionalService.obtenerPorId(1L);

        assertEquals("Juan Perez", dto.getNombre());
        verify(profesionalRepository, times(1)).findById(1L);
    }

    @Test
    void testObtenerPorId_NoEncontrado() {
        when(profesionalRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, ()-> profesionalService.obtenerPorId(1L));     
    }

    @Test
    void testActualizarProfesional() {
        when(profesionalRepository.findById(1L)).thenReturn(Optional.of(profesional));
        when(profesionalRepository.save(any(Profesional.class))).thenReturn(profesional);
        
        ProfesionalDTO dto = new ProfesionalDTO();
        dto.setNombre("Juan Actualizado");
        dto.setEmail("juan@nuevo.com");
        dto.setEspecialidad("Podologia");

        ProfesionalDTO actualizado = profesionalService.actualizarProfesional(1L, dto);
        assertEquals("Juan Actualizado", actualizado.getNombre());
    }

     @Test
    void testEliminarProfesional() {
        when(profesionalRepository.findById(1L)).thenReturn(Optional.of(profesional));

        profesionalService.eliminarProfesional(1L);

        verify(profesionalRepository, times(1)).delete(profesional);
    }

}
