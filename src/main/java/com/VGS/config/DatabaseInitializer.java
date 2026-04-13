package com.VGS.config;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.Statement;

/**
 * Initializes the SQLite database when the application starts.
 * Creates the required tables if they do not already exist.
 */
@Component
public class DatabaseInitializer {

    private final String dbPath;

    /**
     * Constructor for DatabaseInitializer.
     * Receives the database file path provided by the user.
     *
     * @param dbPath the SQLite database file path (provided at runtime)
     */
    public DatabaseInitializer(String dbPath) {
        this.dbPath = dbPath;
    }

    /**
     * Automatically runs after the bean is created.
     * Creates the "games" table in the SQLite database if it does not exist.
     *
     * This ensures the database is properly structured before the application is used.
     */
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