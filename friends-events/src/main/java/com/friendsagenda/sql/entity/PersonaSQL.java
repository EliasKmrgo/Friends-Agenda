package com.friendsagenda.sql.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.Instant;
import java.time.Period;
import jakarta.persistence.*;

@Entity
@Table(name = "persona")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonaSQL {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "canonical_id", length = 64, unique = true, nullable = false)
    private String canonicalId;

    @Column(nullable = false, length = 200)
    private String nombre;

    @Column(name = "email_principal", nullable = false, length = 200)
    private String emailPrincipal;

    private String ciudad;
    private String pais;

    @Column(name = "fecha_nac")
    private LocalDate fechaNac;

    @Column(name = "actualizado_en")
    private Instant actualizadoEn;

    @Column(name = "schema_version", nullable = false)
    private Integer schemaVersion;

    public int getEdad() {
        if (fechaNac == null) return 0;
        return Period.between(fechaNac, LocalDate.now()).getYears();
    }
}
