package com.friendsagenda.service;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.friendsagenda.document.PersonDoc;
import com.friendsagenda.document.PersonDoc.Email;
import com.friendsagenda.document.PersonDoc.Location;
import com.friendsagenda.document.PersonDoc.Name;
import com.friendsagenda.repository.PersonRepository;
import com.friendsagenda.util.CanonicalId;
import com.friendsagenda.validation.JsonSchemaValidator;

/**
 * Servicio de ingesta y normalización de datos de Persona.
 * <p>
 * Expone operaciones para almacenar ingestas crudas y normalizar/upsert de
 * personas canónicas.
 */
@Service
public class IngestService {

    private final MongoTemplate mongoTemplate;
    private final PersonRepository personRepository;
    private final JsonSchemaValidator validator;

    /**
     * Crea una nueva instancia del servicio de ingesta.
     *
     * @param mongoTemplate    plantilla de acceso a MongoDB
     * @param personRepository repositorio de personas
     * @param validator        validador JSON Schema (stub)
     */
    public IngestService(
            MongoTemplate mongoTemplate,
            PersonRepository personRepository,
            JsonSchemaValidator validator) {
        this.mongoTemplate = mongoTemplate;
        this.personRepository = personRepository;
        this.validator = validator;
    }

    /**
     * Ingresa un payload crudo en la colección {@code raw_ingest} con metadata.
     *
     * @param source  origen de la ingesta (query param)
     * @param payload mapa crudo recibido en el cuerpo
     */
    public void ingestRaw(String source, Map<String, Object> payload) {
        Map<String, Object> toStore = new HashMap<>();
        toStore.put("source", source);
        toStore.put("payload", payload);
        toStore.put("receivedAt", Instant.now());
        mongoTemplate.insert(toStore, "raw_ingest");
    }

    /**
     * Normaliza los datos básicos de persona y realiza upsert por {@code canonicalId}.
     * <p>
     * Reglas:
     * - canonicalId = sha256("person|" + email.toLowerCase()).
     * - Construye {@link PersonDoc} con schemaVersion=1, Name(full), Email(email),
     *   Location(ciudad, "CO"), y {@code sourceMeta}.
     * - Valida contra schema JSON canónico usando el stub.
     * - Upsert: si existe por canonicalId, conserva {@code id} y actualiza
     *   {@code updatedAt}; si no existe, inserta.
     *
     * @param nombre nombre completo
     * @param correo email de la persona
     * @param ciudad ciudad declarada
     * @param source origen de los datos
     * @return documento canónico persistido
     */
    public PersonDoc normalizeAndUpsertPerson(String nombre, String correo, String ciudad, String source) {
        String normalizedEmail = correo == null ? null : correo.toLowerCase();
        String canonicalId = CanonicalId.sha256("person|" + normalizedEmail);

        Name name = new Name(nombre);
        Email email = new Email(correo);
        Location location = new Location(ciudad, "CO");

        Map<String, Object> sourceMeta = new HashMap<>();
        sourceMeta.put("source", source);

        PersonDoc doc = new PersonDoc();
        doc.setCanonicalId(canonicalId);
        doc.setSchemaVersion(1);
        doc.setName(name);
        doc.setEmails(List.of(email));
        doc.setLocation(location);
        doc.setSourceMeta(sourceMeta);
        doc.setUpdatedAt(Instant.now());

        // Validación con stub (asegura que el recurso del esquema existe)
        validator.validate("json-schema/canonical-person.schema.json", doc);

        Optional<PersonDoc> existing = personRepository.findByCanonicalId(canonicalId);
        if (existing.isPresent()) {
            PersonDoc current = existing.get();
            doc.setId(current.getId());
            doc.setUpdatedAt(Instant.now());
        }
        return personRepository.save(doc);
    }
}

