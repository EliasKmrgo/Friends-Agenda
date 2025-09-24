package com.appevents.domain.dto;

import jakarta.validation.constraints.NotBlank;

public record CrearLugarDTO(
        @NotBlank String direccion,
        @NotBlank String ciudad
) {}

