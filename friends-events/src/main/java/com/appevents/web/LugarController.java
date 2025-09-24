package com.appevents.web;

import com.appevents.application.LugarService;
import com.appevents.domain.dto.CrearLugarDTO;
import com.appevents.domain.model.Lugar;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/lugares")
@RequiredArgsConstructor
public class LugarController {
    private final LugarService lugarService;

    @GetMapping
    public List<Lugar> listar() {
        return lugarService.listar();
    }

    @PostMapping
    public ResponseEntity<Lugar> crear(@Valid @RequestBody CrearLugarDTO dto) {
        var creado = lugarService.crear(dto);
        return ResponseEntity.created(URI.create("/api/lugares/" + creado.getId())).body(creado);
    }
}

