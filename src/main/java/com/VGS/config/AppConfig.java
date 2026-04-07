package com.VGS.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    /**
     * Provides the SQLite database path as a Spring Bean.
     * This allows GameService or any other class to @Autowired this String.
     */
    @Bean
    public String dbPath() {
        // You can change this path to wherever your SQLite file is located
        return "games.db";
    }
}
