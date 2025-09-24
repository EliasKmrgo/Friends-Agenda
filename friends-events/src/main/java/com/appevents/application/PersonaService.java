package com.appevents.application;

import com.appevents.domain.dto.AgregarAmigoDTO;
import com.appevents.domain.dto.CrearContactoDTO;
import com.appevents.domain.dto.CrearPersonaDTO;
import com.appevents.domain.model.Contacto;
import com.appevents.domain.model.Persona;
import com.appevents.infrastructure.repository.mem.ContactoMemRepository;
import com.appevents.infrastructure.repository.mem.MemRelations;
import com.appevents.infrastructure.repository.mem.HobbyMemRepository;
import com.appevents.infrastructure.repository.mem.PersonaMemRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PersonaService {

    private final PersonaMemRepository personaRepo;
    private final ContactoMemRepository contactoRepo;
    private final MemRelations relations;
    private final HobbyMemRepository hobbyRepo;

    public PersonaService(PersonaMemRepository personaRepo,
                          ContactoMemRepository contactoRepo,
                          MemRelations relations,
                          HobbyMemRepository hobbyRepo) {
        this.personaRepo = personaRepo;
        this.contactoRepo = contactoRepo;
        this.relations = relations;
        this.hobbyRepo = hobbyRepo;
    }

    public List<Persona> listar() {
        return personaRepo.findAll();
    }

    public Persona crear(CrearPersonaDTO dto) {
        var p = new Persona(null, dto.nombre(), dto.fechaNac());
        return personaRepo.save(p);
    }

    public Contacto agregarContacto(Long personaId, CrearContactoDTO dto) {
        var persona = personaRepo.findById(personaId)
                .orElseThrow(() -> new NoSuchElementException("Persona no encontrada"));
        var c = new Contacto(null, dto.tipo(), dto.valor(), persona.getId());
        return contactoRepo.save(c);
    }

    public void agregarAmigo(AgregarAmigoDTO dto) {
        var a = dto.personaId();
        var b = dto.amigoId();
        if (a.equals(b)) {
            throw new IllegalArgumentException("No se permite auto-amistad");
        }
        // Normalizar par (min, max)
        long min = Math.min(a, b);
        long max = Math.max(a, b);

        // Validar existencia
        personaRepo.findById(min).orElseThrow(() -> new NoSuchElementException("Persona no encontrada"));
        personaRepo.findById(max).orElseThrow(() -> new NoSuchElementException("Amigo no encontrado"));

        relations.amigosPorPersona.computeIfAbsent(min, k -> ConcurrentHashMap.newKeySet()).add(max);
        relations.amigosPorPersona.computeIfAbsent(max, k -> ConcurrentHashMap.newKeySet()).add(min);
    }

    public void asignarHobby(Long personaId, Long hobbyId) {
        personaRepo.findById(personaId).orElseThrow(() -> new NoSuchElementException("Persona no encontrada"));
        hobbyRepo.findById(hobbyId).orElseThrow(() -> new NoSuchElementException("Hobby no encontrado"));
        relations.hobbiesPorPersona.computeIfAbsent(personaId, k -> ConcurrentHashMap.newKeySet()).add(hobbyId);
    }

    public Set<Long> getAmigos(Long personaId) {
        return relations.amigosPorPersona.getOrDefault(personaId, ConcurrentHashMap.newKeySet());
    }
}
