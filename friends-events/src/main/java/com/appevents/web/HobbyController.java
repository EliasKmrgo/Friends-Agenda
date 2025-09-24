package com.appevents.web;

import com.appevents.application.HobbyService;
import com.appevents.domain.dto.CrearHobbyDTO;
import com.appevents.domain.model.Hobby;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/hobbies")
@RequiredArgsConstructor
public class HobbyController {
    private final HobbyService hobbyService;

    @GetMapping
    public List<Hobby> listar() {
        return hobbyService.listar();
    }

    @PostMapping
    public ResponseEntity<Hobby> crear(@Valid @RequestBody CrearHobbyDTO dto) {
        var creado = hobbyService.crear(dto);
        return ResponseEntity.created(URI.create("/api/hobbies/" + creado.getId())).body(creado);
    }
}

