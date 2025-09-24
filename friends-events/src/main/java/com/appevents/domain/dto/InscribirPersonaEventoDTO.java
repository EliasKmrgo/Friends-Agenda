package com.appevents.domain.dto;

import jakarta.validation.constraints.NotNull;

public record InscribirPersonaEventoDTO(
        @NotNull Long personaId
) {}

