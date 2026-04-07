package com.VGS.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.util.Scanner;

@Configuration
public class AppConfig {

    /* Provides the SQLite database path as a Spring Bean.
     * This allows GameService or any other class to @Autowired this String.
     */
    @Bean
    public String dbPath() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Enter SQLite database file path:");
            String input = scanner.nextLine().trim();

            // Blank check
            if (input.isEmpty()) {
                System.out.println("Path cannot be blank. Try again.");
                continue;
            }

            //  Try connecting
            try (Connection conn = DatabaseConnection.connect(input)) {
                if (conn != null) {
                    System.out.println("Database connected successfully!");
                    return input;
                }
            } catch (Exception e) {
                System.out.println("Invalid database path. Try again.");
            }
        }
    }
}
