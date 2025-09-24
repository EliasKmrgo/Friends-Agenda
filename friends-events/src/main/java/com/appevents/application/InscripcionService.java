package com.appevents.application;

import com.appevents.domain.dto.InscribirDTO;
import com.appevents.domain.model.Inscripcion;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Servicio de aplicación para gestionar inscripciones en memoria.
 */
public class InscripcionService {

    private final Map<String, Inscripcion> inscripciones = new ConcurrentHashMap<>();

    /**
     * Registra una inscripción validando que los campos requeridos no estén vacíos.
     */
    public Inscripcion inscribir(InscribirDTO dto) {
        if (dto.personaId() == null || dto.personaId().isBlank()) {
            throw new IllegalArgumentException("personaId es requerido");
        }
        if (dto.eventoId() == null || dto.eventoId().isBlank()) {
            throw new IllegalArgumentException("eventoId es requerido");
        }

        var id = UUID.randomUUID().toString();
        var now = LocalDateTime.now();
        var ins = new Inscripcion(id, dto.personaId(), dto.eventoId(), now, "REGISTRADA");
        inscripciones.put(id, ins);
        return ins;
    }

    /** Lista las inscripciones asociadas a un evento. */
    public List<Inscripcion> listarPorEvento(String eventoId) {
        return inscripciones.values().stream()
                .filter(i -> i.getEventoId().equals(eventoId))
                .collect(Collectors.toList());
    }
}

