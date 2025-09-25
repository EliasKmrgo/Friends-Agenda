package com.appevents;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication(scanBasePackages = {"com.friendsagenda", "com.appevents"})
@EnableMongoRepositories(basePackages = "com.friendsagenda.mongo.repository")
@EnableJpaRepositories(basePackages = "com.friendsagenda.sql.repository")
@EntityScan(basePackages = "com.friendsagenda.sql.entity") 

public class AppEventsApplication {
    public static void main(String[] args) {
        SpringApplication.run(AppEventsApplication.class, args);
    }
}
