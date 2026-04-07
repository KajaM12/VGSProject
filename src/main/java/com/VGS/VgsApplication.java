package com.VGS;

import com.VGS.config.DatabaseInitializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// My GUI Web Interface Link: http://localhost:8080

@SpringBootApplication
// 1. Marks this class as the main Spring Boot application.
// 2. Enables component scanning (so Spring will detect @Controller, @Service, etc.).
// 3. Enables auto-configuration to automatically set up the Spring environment based on dependencies.

public class VgsApplication {

    public static void main(String[] args) {
        String dbPath = "games.db"; // Can be configured in application.properties
        DatabaseInitializer.initialize(dbPath);
        SpringApplication.run(VgsApplication.class, args);
    }
}
