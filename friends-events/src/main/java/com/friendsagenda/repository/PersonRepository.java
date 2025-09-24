package com.friendsagenda.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.friendsagenda.document.PersonDoc;

/**
 * Spring Data MongoDB repository for {@link PersonDoc}.
 * <p>
 * Provides basic CRUD operations and a finder by canonical identifier.
 */
public interface PersonRepository extends MongoRepository<PersonDoc, String> {

    /**
     * Finds a person document by its canonical identifier.
     *
     * @param canonicalId SHA-256 hex canonical identifier (64 chars)
     * @return an {@link Optional} with the matching document if present
     */
    Optional<PersonDoc> findByCanonicalId(String canonicalId);
}

