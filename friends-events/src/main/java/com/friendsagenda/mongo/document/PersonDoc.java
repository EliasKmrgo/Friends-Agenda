package com.friendsagenda.mongo.document;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * MongoDB document model representing a canonical Person.
 * <p>
 * Collection name: {@code persons}. Includes a Mongo {@code id}, a
 * deterministic {@code canonicalId} (typically SHA-256 hex), schema version,
 * name, emails, optional location, source metadata and the last update
 * timestamp.
 */
@Document(collection = "persons")
public class PersonDoc {

    /** MongoDB document identifier. */
    @Id
    private String id;

    /** Canonical SHA-256 hex identifier (64 chars). */
    private String canonicalId;

    /** Canonical schema version for the stored document. */
    private Integer schemaVersion;

    /** Person full name container. */
    private Name name;

    /** List of email entries. */
    private List<Email> emails;

    /** Optional location details. */
    private Location location;

    /** Arbitrary source metadata. */
    private Map<String, Object> sourceMeta;

    /** Last update timestamp in UTC. */
    private Instant updatedAt;

    /**
     * Empty constructor required by frameworks and serializers.
     */
    public PersonDoc() {
    }

    /**
     * Full constructor to build a complete {@link PersonDoc} instance.
     *
     * @param id            MongoDB identifier
     * @param canonicalId   canonical SHA-256 identifier
     * @param schemaVersion schema version integer
     * @param name          name record containing the full name
     * @param emails        list of email records
     * @param location      optional location record
     * @param sourceMeta    arbitrary metadata map
     * @param updatedAt     last update instant
     */
    public PersonDoc(
            String id,
            String canonicalId,
            Integer schemaVersion,
            Name name,
            List<Email> emails,
            Location location,
            Map<String, Object> sourceMeta,
            Instant updatedAt) {
        this.id = id;
        this.canonicalId = canonicalId;
        this.schemaVersion = schemaVersion;
        this.name = name;
        this.emails = emails;
        this.location = location;
        this.sourceMeta = sourceMeta;
        this.updatedAt = updatedAt;
    }

    /**
     * MongoDB identifier getter.
     *
     * @return the {@code id}
     */
    public String getId() {
        return id;
    }

    /**
     * MongoDB identifier setter.
     *
     * @param id the document id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Canonical identifier getter.
     *
     * @return the {@code canonicalId}
     */
    public String getCanonicalId() {
        return canonicalId;
    }

    /**
     * Canonical identifier setter.
     *
     * @param canonicalId the canonical id to set (64 hex chars)
     */
    public void setCanonicalId(String canonicalId) {
        this.canonicalId = canonicalId;
    }

    /**
     * Schema version getter.
     *
     * @return the {@code schemaVersion}
     */
    public Integer getSchemaVersion() {
        return schemaVersion;
    }

    /**
     * Schema version setter.
     *
     * @param schemaVersion the schema version to set
     */
    public void setSchemaVersion(Integer schemaVersion) {
        this.schemaVersion = schemaVersion;
    }

    /**
     * Name getter.
     *
     * @return the name record
     */
    public Name getName() {
        return name;
    }

    /**
     * Name setter.
     *
     * @param name the name record to set
     */
    public void setName(Name name) {
        this.name = name;
    }

    /**
     * Emails getter.
     *
     * @return list of email records
     */
    public List<Email> getEmails() {
        return emails;
    }

    /**
     * Emails setter.
     *
     * @param emails the list of email records to set
     */
    public void setEmails(List<Email> emails) {
        this.emails = emails;
    }

    /**
     * Location getter.
     *
     * @return the optional location record
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Location setter.
     *
     * @param location the optional location record to set
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * Source metadata getter.
     *
     * @return the metadata map
     */
    public Map<String, Object> getSourceMeta() {
        return sourceMeta;
    }

    /**
     * Source metadata setter.
     *
     * @param sourceMeta the metadata map to set
     */
    public void setSourceMeta(Map<String, Object> sourceMeta) {
        this.sourceMeta = sourceMeta;
    }

    /**
     * Update timestamp getter.
     *
     * @return the last update instant
     */
    public Instant getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Update timestamp setter.
     *
     * @param updatedAt the last update instant to set
     */
    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * Minimal name value object.
     *
     * @param full full display name
     */
    public static record Name(String full) {}

    /**
     * Email value object.
     *
     * @param value email address value
     */
    public static record Email(String value) {}

    /**
     * Optional location value object.
     *
     * @param city    city name (nullable)
     * @param country country name (nullable)
     */
    public static record Location(String city, String country) {}
}

