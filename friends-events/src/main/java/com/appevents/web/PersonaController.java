package com.appevents.web;

import com.appevents.application.PersonaService;
import com.appevents.domain.dto.AgregarAmigoDTO;
import com.appevents.domain.dto.CrearContactoDTO;
import com.appevents.domain.dto.CrearPersonaDTO;
import com.appevents.domain.model.Contacto;
import com.appevents.domain.model.Persona;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/personas")
@RequiredArgsConstructor
public class PersonaController {
    private final PersonaService personaService;

    @GetMapping
    public List<Persona> listar() {
        return personaService.listar();
    }

    @PostMapping
    public ResponseEntity<Persona> crear(@Valid @RequestBody CrearPersonaDTO dto) {
        var creada = personaService.crear(dto);
        return ResponseEntity.created(URI.create("/api/personas/" + creada.getId())).body(creada);
    }

    @PostMapping("/{id}/contactos")
    public ResponseEntity<Contacto> agregarContacto(@PathVariable Long id, @Valid @RequestBody CrearContactoDTO dto) {
        var contacto = personaService.agregarContacto(id, dto);
        return ResponseEntity.created(URI.create("/api/personas/" + id + "/contactos/" + contacto.getId())).body(contacto);
    }

    @PostMapping("/amigos")
    public ResponseEntity<Void> agregarAmigo(@Valid @RequestBody AgregarAmigoDTO dto) {
        personaService.agregarAmigo(dto);
        return ResponseEntity.status(201).build();
    }

    @PostMapping("/{id}/hobbies/{hobbyId}")
    public ResponseEntity<Void> asignarHobby(@PathVariable Long id, @PathVariable Long hobbyId) {
        personaService.asignarHobby(id, hobbyId);
        return ResponseEntity.status(201).build();
    }
}

