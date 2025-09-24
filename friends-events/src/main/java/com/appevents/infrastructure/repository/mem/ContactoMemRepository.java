package com.appevents.infrastructure.repository.mem;

import com.appevents.domain.model.Contacto;
import org.springframework.stereotype.Component;

@Component
public class ContactoMemRepository extends BaseMemRepository<Contacto> {
    public ContactoMemRepository() {
        super(Contacto::getId, Contacto::setId);
    }
}

