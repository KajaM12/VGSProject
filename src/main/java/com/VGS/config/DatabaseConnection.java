package com.VGS.config;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Handles the connection to the SQLite database.
 * Provides a method to establish a connection using a user-provided database path.
 */
public class DatabaseConnection {

    /**
     * Establishes a connection to the SQLite database using the provided file path.
     *
     * The database path is supplied by the user at runtime, ensuring the application
     * does not rely on any hardcoded file locations.
     *
     * @param dbPath the file path to the SQLite database provided by the user
     * @return a Connection object if successful, or null if the connection fails
     */
    public static Connection connect(String dbPath) {
        try {
            return DriverManager.getConnection("jdbc:sqlite:" + dbPath);
        } catch (Exception e) {
            System.out.println("Database connection failed: " + e.getMessage());
            return null;
        }
    }
}