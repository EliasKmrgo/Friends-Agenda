package com.appevents;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Arranque de la aplicación.
 * Se amplía el escaneo de componentes y repositorios para incluir com.friendsagenda.*
 */
@SpringBootApplication(
    scanBasePackages = {
        "com.appevents",          // tu app actual
        "com.friendsagenda"       // documentos, servicios, controladores y repos
    }
)
@EnableMongoRepositories(basePackages = "com.friendsagenda.repository")
public class AppEventsApplication {
    public static void main(String[] args) {
        SpringApplication.run(AppEventsApplication.class, args);
    }
}
