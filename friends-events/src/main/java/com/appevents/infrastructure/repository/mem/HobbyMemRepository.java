package com.appevents.infrastructure.repository.mem;

import com.appevents.domain.model.Hobby;
import org.springframework.stereotype.Component;

@Component
public class HobbyMemRepository extends BaseMemRepository<Hobby> {
    public HobbyMemRepository() {
        super(Hobby::getId, Hobby::setId);
    }
}

