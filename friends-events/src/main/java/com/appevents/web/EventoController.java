package com.appevents.web;

import com.appevents.application.EventoService;
import com.appevents.domain.dto.CrearEventoDTO;
import com.appevents.domain.model.Evento;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/eventos")
@RequiredArgsConstructor
public class EventoController {

    private final EventoService eventoService;

    @PostMapping
    public ResponseEntity<Evento> crear(@Valid @RequestBody CrearEventoDTO dto) {
        var creado = eventoService.crear(dto);
        return ResponseEntity.created(URI.create("/api/eventos/" + creado.getId())).body(creado);
    }

    @GetMapping
    public List<Evento> listar() {
        return eventoService.listar();
    }

    @GetMapping("/{id}")
    public Evento obtener(@PathVariable String id) {
        return eventoService.obtener(id)
                .orElseThrow(() -> new NoSuchElementException("Evento no encontrado"));
    }

    // ===== Ejemplos cURL =====
    // # Crear evento
    // curl -X POST http://localhost:8080/api/eventos \
    //   -H "Content-Type: application/json" \
    //   -d '{"titulo":"Kickoff","fecha":"2025-10-01","duracionMin":90,"direccion":"Cra 1 #23-45","ciudad":"Bogotá","pais":"CO"}'
    //
    // # Listar eventos
    // curl http://localhost:8080/api/eventos
}

