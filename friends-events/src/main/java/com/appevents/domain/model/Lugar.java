package com.appevents.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Value Object que describe un lugar.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lugar {
    private String direccion;
    private String ciudad;
    private String pais;
}

