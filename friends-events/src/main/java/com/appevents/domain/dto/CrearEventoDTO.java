package com.appevents.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

/**
 * DTO para la creación de eventos.
 */
public record CrearEventoDTO(
        @NotBlank String titulo,
        @NotNull LocalDate fecha,
        @NotNull Long duracionMin,
        @NotBlank String direccion,
        String ciudad,
        String pais
) {}

