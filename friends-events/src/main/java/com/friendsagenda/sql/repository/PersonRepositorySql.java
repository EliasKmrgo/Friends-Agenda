package com.friendsagenda.sql.repository;

import com.friendsagenda.sql.entity.PersonaSQL;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonRepositorySql extends JpaRepository<PersonaSQL, Long> {
    Optional<PersonaSQL> findByCanonicalId(String canonicalId);
}