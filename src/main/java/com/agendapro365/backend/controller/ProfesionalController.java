package com.agendapro365.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.agendapro365.backend.dto.ProfesionalDTO;
import com.agendapro365.backend.service.ProfesionalService;

@RestController
@RequestMapping("/api/profesionales")
public class ProfesionalController {

    private final ProfesionalService profesionalService;

    public ProfesionalController(ProfesionalService profesionalService) {
        this.profesionalService = profesionalService;
    }

    @PostMapping
    public ResponseEntity<ProfesionalDTO> crearProfesional(@RequestBody ProfesionalDTO dto) {
        ProfesionalDTO creado = profesionalService.crearProfesional(dto);
        return ResponseEntity.ok(creado);
    }


    @GetMapping
    public ResponseEntity<List<ProfesionalDTO>> listarProfesionales() {
        return ResponseEntity.ok(profesionalService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfesionalDTO> obtenerPorId(@PathVariable Long id) {
        ProfesionalDTO dto = profesionalService.obtenerPorId(id);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfesionalDTO> actualizarProfesional(@PathVariable Long id, @RequestBody ProfesionalDTO dto) {
        ProfesionalDTO actualizado = profesionalService.actualizarProfesional(id, dto);
       return ResponseEntity.ok(actualizado);   
    }

    @DeleteMapping("/{id}")

    public ResponseEntity<Void> eliminarProfesional(@PathVariable Long id) {
        profesionalService.eliminarProfesional(id);
        return ResponseEntity.noContent().build();
    }
}
