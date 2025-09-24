package com.appevents.domain.dto;

import jakarta.validation.constraints.NotBlank;

public record CrearContactoDTO(
        @NotBlank String tipo,
        @NotBlank String valor
) {}

