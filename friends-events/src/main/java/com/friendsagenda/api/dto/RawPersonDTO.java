package com.friendsagenda.api.dto;

/** Payload crudo típico para normalizar una Persona. */
public record RawPersonDTO(String nombre, String correo, String ciudad, String source) {}

