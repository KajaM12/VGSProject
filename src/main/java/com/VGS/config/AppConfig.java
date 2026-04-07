package com.VGS.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Scanner;

@Configuration
public class AppConfig {

    /* Provides the SQLite database path as a Spring Bean.
     * This allows GameService or any other class to @Autowired this String.
     */
    @Bean
    public String dbPath() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter SQLite database file path:");
        return scanner.nextLine();
    }
}
