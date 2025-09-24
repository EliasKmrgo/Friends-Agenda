package com.appevents.domain.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO para registrar la inscripción de una persona a un evento.
 */
public record InscribirDTO(
        @NotBlank String personaId,
        @NotBlank String eventoId
) {}

