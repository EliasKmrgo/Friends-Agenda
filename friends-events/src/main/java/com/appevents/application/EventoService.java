package com.appevents.application;

import com.appevents.domain.dto.CrearEventoDTO;
import com.appevents.domain.model.Evento;
import com.appevents.domain.model.Lugar;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Servicio de aplicación para gestionar eventos en memoria.
 */
public class EventoService {

    private final Map<String, Evento> eventos = new ConcurrentHashMap<>();

    /**
     * Crea un evento a partir del DTO, generando un ID y guardándolo en memoria.
     */
    public Evento crear(CrearEventoDTO dto) {
        var id = UUID.randomUUID().toString();
        var lugar = new Lugar(dto.direccion(), dto.ciudad(), dto.pais());
        var duracion = Duration.ofMinutes(dto.duracionMin());
        var evento = new Evento(id, dto.titulo(), dto.fecha(), duracion, lugar);
        eventos.put(id, evento);
        return evento;
    }

    /** Obtiene un evento por ID. */
    public Optional<Evento> obtener(String id) {
        return Optional.ofNullable(eventos.get(id));
    }

    /** Lista todos los eventos. */
    public List<Evento> listar() {
        return new ArrayList<>(eventos.values());
    }
}

