package com.VGS.config;

import com.VGS.service.GameService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Configuration class to load initial data into the application.
// GameService used to load games into the repository
// Returning a CommandLineRunner that executes on application startup
@Configuration
    public class DataLoader {

    @Bean
    CommandLineRunner loadData(GameService service) {
        // Lambda expression that runs when the app starts
        return args -> {
            // Call the service to load games from "games.txt" in resources
            int count = service.loadGamesFromFile("games.txt");
            // Print how many games were successfully loaded
            System.out.println(count + " games loaded.");
        };
    }
}
