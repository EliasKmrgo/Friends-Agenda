package com.appevents.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CrearHobbyDTO(
        @NotBlank String nombre,
        @Size(max = 200) String descripcion
) {}

