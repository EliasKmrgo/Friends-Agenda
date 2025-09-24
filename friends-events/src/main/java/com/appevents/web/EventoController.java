package com.appevents.web;

import com.appevents.application.EventoService;
import com.appevents.domain.dto.CrearEventoDTO;
import com.appevents.domain.dto.InscribirPersonaEventoDTO;
import com.appevents.domain.model.Evento;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

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

    @PostMapping("/{id}/participantes")
    public ResponseEntity<Void> inscribir(@PathVariable Long id, @Valid @RequestBody InscribirPersonaEventoDTO dto) {
        eventoService.inscribirParticipante(id, dto);
        return ResponseEntity.created(URI.create("/api/eventos/" + id + "/participantes")).build();
    }
}
