package com.appevents.application;

import com.appevents.domain.dto.CrearEventoDTO;
import com.appevents.domain.dto.InscribirPersonaEventoDTO;
import com.appevents.domain.model.Evento;
import com.appevents.infrastructure.repository.mem.EventoMemRepository;
import com.appevents.infrastructure.repository.mem.LugarMemRepository;
import com.appevents.infrastructure.repository.mem.MemRelations;
import com.appevents.infrastructure.repository.mem.PersonaMemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class EventoService {

    private final EventoMemRepository eventoRepo;
    private final LugarMemRepository lugarRepo;
    private final MemRelations relations;
    private final PersonaMemRepository personaRepo;

    public EventoService(EventoMemRepository eventoRepo, LugarMemRepository lugarRepo, MemRelations relations, PersonaMemRepository personaRepo) {
        this.eventoRepo = eventoRepo;
        this.lugarRepo = lugarRepo;
        this.relations = relations;
        this.personaRepo = personaRepo;
    }

    public List<Evento> listar() {
        return eventoRepo.findAll();
    }

    public Evento crear(CrearEventoDTO dto) {
        if (dto.fin().isBefore(dto.inicio())) {
            throw new IllegalArgumentException("fechaFin debe ser >= fechaInicio");
        }
        // validar lugar
        lugarRepo.findById(dto.lugarId()).orElseThrow(() -> new NoSuchElementException("Lugar no encontrado"));
        var e = new Evento(null, dto.titulo(), dto.descripcion(), dto.inicio(), dto.fin(), dto.lugarId());
        return eventoRepo.save(e);
    }

    public void inscribirParticipante(Long eventoId, InscribirPersonaEventoDTO dto) {
        // validar evento y persona
        eventoRepo.findById(eventoId).orElseThrow(() -> new NoSuchElementException("Evento no encontrado"));
        personaRepo.findById(dto.personaId()).orElseThrow(() -> new NoSuchElementException("Persona no encontrada"));
        relations.participantesPorEvento
                .computeIfAbsent(eventoId, k -> ConcurrentHashMap.newKeySet())
                .add(dto.personaId());
    }

    public Set<Long> participantes(Long eventoId) {
        return relations.participantesPorEvento.getOrDefault(eventoId, ConcurrentHashMap.newKeySet());
    }
}
