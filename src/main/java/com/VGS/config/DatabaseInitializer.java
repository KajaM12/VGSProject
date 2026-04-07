package com.VGS.config;

import java.sql.Connection;
import java.sql.Statement;

public class DatabaseInitializer {

    public static void initialize(String dbPath) {
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