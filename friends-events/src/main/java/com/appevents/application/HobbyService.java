package com.appevents.application;

import com.appevents.domain.dto.CrearHobbyDTO;
import com.appevents.domain.model.Hobby;
import com.appevents.infrastructure.repository.mem.HobbyMemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HobbyService {
    private final HobbyMemRepository hobbyRepo;

    public HobbyService(HobbyMemRepository hobbyRepo) {
        this.hobbyRepo = hobbyRepo;
    }

    public List<Hobby> listar() {
        return hobbyRepo.findAll();
    }

    public Hobby crear(CrearHobbyDTO dto) {
        var h = new Hobby(null, dto.nombre(), dto.descripcion());
        return hobbyRepo.save(h);
    }
}

