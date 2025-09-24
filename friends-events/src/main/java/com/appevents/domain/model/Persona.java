package com.appevents.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Persona {
    private String id;
    private String nombre;
    private LocalDate fechaNacimiento;
    private List<Contacto> contactos = new ArrayList<>();
    private Set<String> hobbyIds = new HashSet<>();

    public int getEdad() {
        if (fechaNacimiento == null) return 0;
        return Period.between(fechaNacimiento, LocalDate.now()).getYears();
    }

    public void addContacto(Contacto c) {
        if (c == null) throw new IllegalArgumentException("Contacto no puede ser nulo");
        if (c.getTipo() == null || c.getTipo().isBlank()) {
            throw new IllegalArgumentException("Tipo de contacto requerido");
        }
        if (c.getValor() == null || c.getValor().isBlank()) {
            throw new IllegalArgumentException("Valor de contacto requerido");
        }
        contactos.add(c);
    }
}

