package com.agendapro365.backend.service;

import java.lang.StackWalker.Option;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;


import com.agendapro365.backend.model.Cita;
import com.agendapro365.backend.model.Cliente;
import com.agendapro365.backend.model.EstadoCita;
import com.agendapro365.backend.model.Profesional;
import com.agendapro365.backend.repository.CitaRepository;
import com.agendapro365.backend.repository.ClienteRepository;
import com.agendapro365.backend.repository.ProfesionalRepository;

@SpringBootTest
@ActiveProfiles("test")
public class CitaServiceIntegrationTest {
   
    @Autowired
    private CitaService citaService;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ProfesionalRepository profesionalRepository;

    @Autowired
    private CitaRepository citaRepository;

    private Cliente cliente;
    private Profesional profesional;

    @BeforeEach
    void setUp() {
        // Guarda datos necesarios para las pruebas en BD H2
        cliente = new Cliente();
        cliente.setNombre("Juan Perez");
        cliente = clienteRepository.save(cliente);

        profesional = new Profesional();
        profesional.setNombre("Dra. Ana");
        profesional = profesionalRepository.save(profesional);
    }

    @Test
    void crearYObtenerCita() {
        // Crear una cita
        Cita cita = new Cita();
        cita.setFechaHora(LocalDateTime.now().plusDays(1));
        cita.setCliente(cliente);
        cita.setProfesional(profesional);
        cita.setEstado(EstadoCita.PENDIENTE);

        // Guardar con repositorio real (BD H2)
        Cita citaGuardada = citaRepository.save(cita);

        // Buscar cita guardada
        Optional<Cita> encontrada = citaRepository.findById(citaGuardada.getId());

         Cita cita2 = encontrada.get();
         System.out.println("ID cita: " + cita2.getId());

        // Verificacion
        assertThat(encontrada).isPresent();
        assertThat(encontrada.get().getEstado()).isEqualTo(EstadoCita.PENDIENTE);
        assertThat(encontrada.get().getCliente().getNombre()).isEqualTo("Juan Perez");
        assertThat(encontrada.get().getProfesional().getNombre()).isEqualTo("Dra. Ana");
    }

}
