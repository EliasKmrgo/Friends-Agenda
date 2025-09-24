package com.appevents.infrastructure.repository.mem;

import com.appevents.domain.model.Lugar;
import org.springframework.stereotype.Component;

@Component
public class LugarMemRepository extends BaseMemRepository<Lugar> {
    public LugarMemRepository() {
        super(Lugar::getId, Lugar::setId);
    }
}

