package com.appevents.infrastructure.repository.mem;

import com.appevents.domain.model.Persona;
import org.springframework.stereotype.Component;

@Component
public class PersonaMemRepository extends BaseMemRepository<Persona> {
    public PersonaMemRepository() {
        super(Persona::getId, Persona::setId);
    }
}

