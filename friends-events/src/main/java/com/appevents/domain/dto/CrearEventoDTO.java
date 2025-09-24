package com.appevents.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

/**
 * DTO para la creación de eventos.
 * El DTO de creación es CrearEventoDTO, que solo contiene los campos mínimos necesarios que el cliente debe enviar para crear un evento nuevo.
 */
public record CrearEventoDTO(
        @NotBlank String titulo,
        @NotBlank String descripcion,
        @NotNull LocalDateTime inicio,
        @NotNull LocalDateTime fin,
        @NotNull Long lugarId
) {}

