package com.appevents.domain.dto;

import jakarta.validation.constraints.NotNull;

public record AgregarAmigoDTO(
        @NotNull Long personaId,
        @NotNull Long amigoId
) {}

