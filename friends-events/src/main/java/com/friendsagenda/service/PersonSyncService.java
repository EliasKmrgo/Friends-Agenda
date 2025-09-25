package com.friendsagenda.service;

import com.friendsagenda.mongo.document.PersonDoc;
import com.friendsagenda.mongo.repository.PersonRepositoryMongo;
import com.friendsagenda.sql.entity.PersonaSQL;
import com.friendsagenda.sql.repository.PersonRepositorySql;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PersonSyncService {

    private final PersonRepositoryMongo mongoRepo;
    private final PersonRepositorySql sqlRepo;

    public PersonSyncService(PersonRepositoryMongo mongoRepo, PersonRepositorySql sqlRepo) {
        this.mongoRepo = mongoRepo;
        this.sqlRepo = sqlRepo;
    }

    @Transactional
    public void syncMongoToSql() {
        List<PersonDoc> mongoPersons = mongoRepo.findAll();

        for (PersonDoc doc : mongoPersons) {
            // Busca si ya existe en SQL por canonicalId
            PersonaSQL persona = sqlRepo.findByCanonicalId(doc.getCanonicalId())
                    .orElse(new PersonaSQL());

            persona.setCanonicalId(doc.getCanonicalId());
            persona.setSchemaVersion(doc.getSchemaVersion());
            persona.setNombre(doc.getName() != null ? doc.getName().full() : "Sin Nombre");
            persona.setEmailPrincipal(
                    doc.getEmails() != null && !doc.getEmails().isEmpty()
                            ? doc.getEmails().get(0).value()
                            : "sin-email@local"
            );
            if (doc.getLocation() != null) {
                persona.setCiudad(doc.getLocation().city());
                persona.setPais(doc.getLocation().country());
            }
            persona.setActualizadoEn(doc.getUpdatedAt());

            sqlRepo.save(persona);
        }

        System.out.println("✅ Sincronización Mongo → SQL completada. Personas sincronizadas: " + mongoPersons.size());
    }
}
