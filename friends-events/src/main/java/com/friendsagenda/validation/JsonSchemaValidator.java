package com.friendsagenda.validation;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import org.springframework.stereotype.Component;
/**
 * Minimal JSON Schema validator stub.
 * <p>
 * This utility only ensures a schema resource exists and is readable from the
 * classpath. It is a placeholder until a real JSON Schema validation library
 * is integrated.
 *
 * TODO: Integrate a JSON Schema library (e.g. everit-org/json-schema or
 * networknt/json-schema-validator) and perform actual payload validation.
 */
@Component
public class JsonSchemaValidator {

    /**
     * Valida un objeto contra el esquema indicado. Implementación stub: solo
     * verifica que el recurso del esquema exista y sea legible en el classpath.
     * No realiza validación estructural.
     *
     * @param classpathLocation ubicación del esquema en el classpath
     * @param data               objeto a validar (no usado en el stub)
     * @throws IllegalArgumentException si el esquema no es legible
     */
    public void validate(String classpathLocation, Object data) {
        if (!isSchemaReadable(classpathLocation)) {
            throw new IllegalArgumentException("Schema not readable: " + classpathLocation);
        }
    }

    /**
     * Loads a schema resource from the classpath and returns {@code true} if the
     * resource exists and is readable.
     *
     * @param classpathLocation classpath location starting with or without a leading slash
     * @return {@code true} if the resource exists and can be read; {@code false} otherwise
     */
    public boolean isSchemaReadable(String classpathLocation) {
        Objects.requireNonNull(classpathLocation, "classpathLocation");
        String normalized = classpathLocation.startsWith("/") ? classpathLocation : "/" + classpathLocation;
        try (InputStream in = JsonSchemaValidator.class.getResourceAsStream(normalized)) {
            if (in == null) {
                return false;
            }
            // Attempt to read a small portion to ensure readability
            byte[] buffer = new byte[1];
            int read = in.read(buffer);
            return read >= 0;
        } catch (IOException ex) {
            return false;
        }
    }
}
