package com.appevents.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.Period;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Persona {
    private Long id;
    private String nombre;
    private LocalDate fechaNac;

    public int getEdad() {
        if (fechaNac == null) return 0;
        return Period.between(fechaNac, LocalDate.now()).getYears();
    }
}
