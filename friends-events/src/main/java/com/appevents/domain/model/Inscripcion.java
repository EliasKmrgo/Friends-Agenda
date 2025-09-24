package com.appevents.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Inscripcion {
    private String id;
    private String personaId;
    private String eventoId;
    private LocalDateTime fechaRegistro;
    private String estado; // REGISTRADA, CANCELADA, ASISTIO
}

