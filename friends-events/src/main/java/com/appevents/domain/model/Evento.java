package com.appevents.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Evento {
    private String id;
    private String titulo;
    private LocalDate fecha;
    private Duration duracion;
    private Lugar lugar;
}

