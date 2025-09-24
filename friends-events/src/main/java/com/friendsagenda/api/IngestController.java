package com.friendsagenda.api;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.friendsagenda.api.dto.RawPersonDTO;
import com.friendsagenda.document.PersonDoc;
import com.friendsagenda.service.IngestService;

/**
 * Controlador REST para endpoints de ingesta.
 */
@RestController
@RequestMapping("/api/ingest")
public class IngestController {

    private final IngestService ingestService;

    /**
     * Constructor de IngestController.
     *
     * @param ingestService servicio de ingesta y normalización
     */
    public IngestController(IngestService ingestService) {
        this.ingestService = ingestService;
    }

    /**
     * Ingesta cruda. Almacena el payload en la colección {@code raw_ingest}.
     *
     * @param source  origen de la ingesta (query param)
     * @param payload mapa JSON recibido en el cuerpo
     * @return 202 Accepted sin cuerpo
     */
    @PostMapping("/raw")
    public ResponseEntity<Void> ingestRaw(
            @RequestParam("source") String source,
            @RequestBody Map<String, Object> payload) {
        ingestService.ingestRaw(source, payload);
        return ResponseEntity.accepted().build();
    }

    /**
     * Normaliza y realiza upsert de una persona por su canonicalId.
     *
     * @param dto payload crudo de persona
     * @return 200 OK con el documento canónico persistido
     */
    @PostMapping("/person")
    public ResponseEntity<PersonDoc> normalizePerson(@RequestBody RawPersonDTO dto) {
        PersonDoc saved = ingestService.normalizeAndUpsertPerson(
                dto.nombre(), dto.correo(), dto.ciudad(), dto.source());
        return ResponseEntity.ok(saved);
    }
}

