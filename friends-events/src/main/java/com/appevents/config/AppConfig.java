package com.appevents.config;

import com.appevents.application.EventoService;
import com.appevents.application.InscripcionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public EventoService eventoService() {
        return new EventoService();
    }

    @Bean
    public InscripcionService inscripcionService() {
        return new InscripcionService();
    }
}

