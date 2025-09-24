package com.appevents.application;

import com.appevents.domain.dto.CrearLugarDTO;
import com.appevents.domain.model.Lugar;
import com.appevents.infrastructure.repository.mem.LugarMemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LugarService {
    private final LugarMemRepository lugarRepo;

    public LugarService(LugarMemRepository lugarRepo) {
        this.lugarRepo = lugarRepo;
    }

    public List<Lugar> listar() {
        return lugarRepo.findAll();
    }

    public Lugar crear(CrearLugarDTO dto) {
        var l = new Lugar(null, dto.direccion(), dto.ciudad());
        return lugarRepo.save(l);
    }
}

