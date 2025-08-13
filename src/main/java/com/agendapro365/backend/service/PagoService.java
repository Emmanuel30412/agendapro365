package com.agendapro365.backend.service;

import com.agendapro365.backend.dto.PagoDTO;
import com.agendapro365.backend.exception.ResourceNotFoundException;
import com.agendapro365.backend.model.Cita;
import com.agendapro365.backend.model.Pago;
import com.agendapro365.backend.repository.CitaRepository;
import com.agendapro365.backend.repository.PagoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PagoService {

    private final PagoRepository pagoRepository;
    private final CitaRepository citaRepository;

    public PagoService(PagoRepository pagoRepository, CitaRepository citaRepository) {
        this.pagoRepository = pagoRepository;
        this.citaRepository = citaRepository;
    }

    public PagoDTO crearPago(PagoDTO dto) {
        Pago pago = toEntity(dto);
        Pago guardado = pagoRepository.save(pago);
        return toDTO(guardado);
    }

    public List<PagoDTO> obtenerTodos() {
        List<Pago> pagos = pagoRepository.findAll();
        return pagos.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public PagoDTO obtenerPorId(Long id) {
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pago no encontrado con id: " + id));
        return toDTO(pago);
    }

    public PagoDTO actualizarPago(Long id, PagoDTO dto) {
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pago no encontrado con id: " + id));

        pago.setMonto(dto.getMonto());
        pago.setMetodoPago(dto.getMetodoPago());
        pago.setFechaPago(dto.getFechaPago());

        if (dto.getCitaId() != null) {
            Cita cita = citaRepository.findById(dto.getCitaId())
                    .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada con id: " + dto.getCitaId()));
            pago.setCita(cita);
        } else {
            pago.setCita(null);
        }

        Pago actualizado = pagoRepository.save(pago);
        return toDTO(actualizado);
    }

    public void eliminarPago(Long id) {
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pago no encontrado con id: " + id));
        pagoRepository.delete(pago);
    }

    private PagoDTO toDTO(Pago pago) {
        PagoDTO dto = new PagoDTO();
        dto.setId(pago.getId());
        dto.setMonto(pago.getMonto());
        dto.setMetodoPago(pago.getMetodoPago());
        dto.setFechaPago(pago.getFechaPago());
        dto.setCitaId(pago.getCita() != null ? pago.getCita().getId() : null);
        return dto;
    }

    private Pago toEntity(PagoDTO dto) {
        Pago pago = new Pago();
        pago.setMonto(dto.getMonto());
        pago.setMetodoPago(dto.getMetodoPago());
        pago.setFechaPago(dto.getFechaPago());

        if (dto.getCitaId() != null) {
            Cita cita = citaRepository.findById(dto.getCitaId())
                    .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada con id: " + dto.getCitaId()));
            pago.setCita(cita);
        }
        return pago;
    }
}
