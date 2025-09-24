package com.appevents.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Value Object que representa un contacto de una persona.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contacto {
    private String tipo;  // EMAIL, PHONE
    private String valor;
}

