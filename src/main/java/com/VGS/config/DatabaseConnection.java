package com.VGS.config;

import java.sql.Connection;
import java.sql.DriverManager;

//Makes sure by database is connected to my App
public class DatabaseConnection {

    public static Connection connect(String dbPath) {
        try {
            return DriverManager.getConnection("jdbc:sqlite:" + dbPath);
        } catch (Exception e) {
            System.out.println("Database connection failed: " + e.getMessage());
            return null;
        }
    }
}