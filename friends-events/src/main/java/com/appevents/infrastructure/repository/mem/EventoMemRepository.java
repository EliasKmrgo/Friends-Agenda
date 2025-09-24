package com.appevents.infrastructure.repository.mem;

import com.appevents.domain.model.Evento;
import org.springframework.stereotype.Component;

@Component
public class EventoMemRepository extends BaseMemRepository<Evento> {
    public EventoMemRepository() {
        super(Evento::getId, Evento::setId);
    }
}

