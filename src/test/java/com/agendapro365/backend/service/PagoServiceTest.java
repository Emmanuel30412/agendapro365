package com.agendapro365.backend.service;

import com.agendapro365.backend.dto.PagoDTO;
import com.agendapro365.backend.exception.ResourceNotFoundException;
import com.agendapro365.backend.model.Cita;
import com.agendapro365.backend.model.Pago;
import com.agendapro365.backend.repository.CitaRepository;
import com.agendapro365.backend.repository.PagoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PagoServiceTest {

    @Mock
    private PagoRepository pagoRepository;

    @Mock
    private CitaRepository citaRepository;

    @InjectMocks
    private PagoService pagoService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCrearPago_Success() {
     // 1. Creamos un objeto DTO con datos de ejemplo para el pago
     PagoDTO dto = new PagoDTO();
     dto.setMonto(new BigDecimal("150.50"));
     dto.setMetodoPago("Tarjeta");
     dto.setFechaPago(LocalDateTime.now());
     dto.setCitaId(1L);

     // 2. Creamos un objeto Cita para simular que existe la cita en BD
     Cita cita = new Cita();
     cita.setId(1L);

     // 3. Simulamos el comportamiento del repositorio de citas: cuando se busca la cita con id 1, devuelve la cita creada arriba
     when(citaRepository.findById(1L)).thenReturn(Optional.of(cita));

     // 4. Preparamos un objeto Pago que simula el pago que guardará el repositorio (como si ya estuviera guardado y con ID generado)
     Pago pagoGuardado = new Pago();
     pagoGuardado.setId(10L);  // simula que la BD le asignó ID 10
     pagoGuardado.setMonto(dto.getMonto());
     pagoGuardado.setMetodoPago(dto.getMetodoPago());
     pagoGuardado.setFechaPago(dto.getFechaPago());
     pagoGuardado.setCita(cita);

     // 5. Simulamos que cuando guardamos un Pago, el repositorio devuelve el pago con ID (como si lo guardara en BD)
     when(pagoRepository.save(any(Pago.class))).thenReturn(pagoGuardado);

     // 6. Ejecutamos el método real que queremos probar: crear un pago usando el dto
     PagoDTO resultado = pagoService.crearPago(dto);

     // 7. Validamos que el resultado no sea nulo (es decir, que el pago se creó y retornó algo)
     assertNotNull(resultado);

     // 8. Validamos que el ID generado sea el esperado (10L)
     assertEquals(10L, resultado.getId());

     // 9. Validamos que el monto del resultado coincida con el del dto enviado
     assertEquals(dto.getMonto(), resultado.getMonto());

     // 10. Validamos que el método de pago coincida
     assertEquals(dto.getMetodoPago(), resultado.getMetodoPago());

     // 11. Validamos que el ID de la cita coincida
     assertEquals(dto.getCitaId(), resultado.getCitaId());

     // 12. Verificamos que el método save del repositorio haya sido llamado realmente (es decir, que se intentó guardar el pago)
     verify(pagoRepository).save(any(Pago.class));
    }

    @Test
    public void testObtenerPorId_Success() {
        Pago pago = new Pago();
        pago.setId(5L);
        pago.setMonto(new BigDecimal("100"));
        pago.setMetodoPago("Efectivo");
        pago.setFechaPago(LocalDateTime.now());

        when(pagoRepository.findById(5L)).thenReturn(Optional.of(pago));

        PagoDTO dto = pagoService.obtenerPorId(5L);

        assertNotNull(dto);
        assertEquals(5L, dto.getId());
        assertEquals(pago.getMonto(), dto.getMonto());
    }

    @Test
    public void testObtenerPorId_NotFound() {
        when(pagoRepository.findById(999L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(ResourceNotFoundException.class, () -> {
            pagoService.obtenerPorId(999L);
        });

        assertEquals("Pago no encontrado con id: 999", ex.getMessage());
    }

    @Test
    public void testActualizarPago_Success() {
        Long pagoId = 3L;

        PagoDTO dto = new PagoDTO();
        dto.setMonto(new BigDecimal("200.75"));
        dto.setMetodoPago("PayPal");
        dto.setFechaPago(LocalDateTime.now());
        dto.setCitaId(2L);

        Pago pagoExistente = new Pago();
        pagoExistente.setId(pagoId);
        pagoExistente.setMonto(new BigDecimal("100"));
        pagoExistente.setMetodoPago("Efectivo");

        Cita cita = new Cita();
        cita.setId(2L);

        when(pagoRepository.findById(pagoId)).thenReturn(Optional.of(pagoExistente));
        when(citaRepository.findById(2L)).thenReturn(Optional.of(cita));
        when(pagoRepository.save(any(Pago.class))).thenAnswer(i -> i.getArgument(0));

        PagoDTO actualizado = pagoService.actualizarPago(pagoId, dto);

        assertNotNull(actualizado);
        assertEquals(dto.getMonto(), actualizado.getMonto());
        assertEquals(dto.getMetodoPago(), actualizado.getMetodoPago());
        assertEquals(dto.getCitaId(), actualizado.getCitaId());

        verify(pagoRepository).save(any(Pago.class));
    }

    @Test
    public void testEliminarPago_Success() {
        Long id = 4L;
        Pago pago = new Pago();
        pago.setId(id);

        when(pagoRepository.findById(id)).thenReturn(Optional.of(pago));
        doNothing().when(pagoRepository).delete(pago);

        pagoService.eliminarPago(id);

        verify(pagoRepository).delete(pago);
    }

    @Test
    public void testEliminarPago_NotFound() {
        when(pagoRepository.findById(100L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(ResourceNotFoundException.class, () -> {
            pagoService.eliminarPago(100L);
        });

        assertEquals("Pago no encontrado con id: 100", ex.getMessage());
    }
}
