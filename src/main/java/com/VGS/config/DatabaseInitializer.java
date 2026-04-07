package com.VGS.config;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.Statement;

// Added a Component and PostConstruct file path for my database to initialize the connection
@Component
public class DatabaseInitializer {

    private final String dbPath;

    public DatabaseInitializer(String dbPath) {
        this.dbPath = dbPath;
    }

    @PostConstruct
    public void initialize() {
        String sql = """
            CREATE TABLE IF NOT EXISTS games (
                id INTEGER PRIMARY KEY,
                title TEXT NOT NULL,
                genre TEXT NOT NULL,
                platform TEXT NOT NULL,
                completed BOOLEAN
            );
        """;

        try (Connection conn = DatabaseConnection.connect(dbPath);
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);

        } catch (Exception e) {
            System.out.println("Table creation failed: " + e.getMessage());
        }
    }
}