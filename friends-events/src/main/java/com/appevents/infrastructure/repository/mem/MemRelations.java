package com.appevents.infrastructure.repository.mem;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MemRelations {
    // Amistades (no dirigidas) representadas por set de amigos por persona
    public final Map<Long, Set<Long>> amigosPorPersona = new ConcurrentHashMap<>();
    // Relación Persona-Hobby
    public final Map<Long, Set<Long>> hobbiesPorPersona = new ConcurrentHashMap<>();
    // Participantes por evento
    public final Map<Long, Set<Long>> participantesPorEvento = new ConcurrentHashMap<>();
}

