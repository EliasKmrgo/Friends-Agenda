package com.appevents.web;

import com.appevents.application.InscripcionService;
import com.appevents.domain.dto.InscribirDTO;
import com.appevents.domain.model.Inscripcion;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/inscripciones")
@RequiredArgsConstructor
public class InscripcionController {

    private final InscripcionService inscripcionService;

    @PostMapping
    public ResponseEntity<Inscripcion> crear(@Valid @RequestBody InscribirDTO dto) {
        var creada = inscripcionService.inscribir(dto);
        return ResponseEntity.created(URI.create("/api/inscripciones/" + creada.getId())).body(creada);
    }

    @GetMapping("/evento/{eventoId}")
    public List<Inscripcion> listarPorEvento(@PathVariable String eventoId) {
        return inscripcionService.listarPorEvento(eventoId);
    }

    // # Inscribir persona a evento
    // curl -X POST http://localhost:8080/api/inscripciones \
    //   -H "Content-Type: application/json" \
    //   -d '{"personaId":"P1","eventoId":"<ID_DEL_EVENTO>"}'
    //
    // # Listar inscripciones por evento
    // curl http://localhost:8080/api/inscripciones/evento/<ID_DEL_EVENTO>
}

