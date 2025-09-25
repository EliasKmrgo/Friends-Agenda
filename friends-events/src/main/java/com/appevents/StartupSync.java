package com.appevents;

import com.friendsagenda.service.PersonSyncService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StartupSync {

    @Bean
    CommandLineRunner runSync(PersonSyncService syncService) {
        return args -> {
            syncService.syncMongoToSql();
        };
    }
}
