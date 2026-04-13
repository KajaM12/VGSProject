package com.VGS.repository;

import com.VGS.config.DatabaseConnection;
import com.VGS.model.Game;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository class responsible for interacting with the SQLite database.
 * Handles all CRUD (Create, Read, Update, Delete) operations for Game objects.
 */

@Repository
public class Gamerepository {

    /**
     * Adds a new game to the database.
     *
     * @param game   The Game object to add
     * @param dbPath The file path to the SQLite database
     * @return true if the game was successfully added, false otherwise
     */
    // Adds a new game to the repository if it is not null and has a unique ID.
    // Game game: The Game object to add.
    // Return: boolean - true if the game was added, false if null or duplicate ID.
     public boolean addGame(Game game, String dbPath) {
         String sql = "INSERT INTO games (id, title, genre, platform, completed) VALUES (?, ?, ?, ?, ?)";
         try (Connection conn = DatabaseConnection.connect(dbPath);
              PreparedStatement stmt = conn.prepareStatement(sql)) {
             stmt.setLong(1, game.getId());
             stmt.setString(2, game.getTitle());
             stmt.setString(3, game.getGenre());
             stmt.setString(4, game.getPlatform());
             stmt.setBoolean(5, game.isCompleted());
             stmt.executeUpdate();
             return true;
         } catch (SQLException e) {
             System.out.println("Add failed: " + e.getMessage());
             return false;
         }
     }

    /**
     * Removes a game from the database by its ID.
     *
     * @param id     The ID of the game to remove
     * @param dbPath The file path to the SQLite database
     * @return true if the game was removed, false if not found or failed
     */
    // Removes a game from the repository based on its ID.
    // Long id: The ID of the game to remove
    public boolean removeGame(long id, String dbPath) {
        String sql = "DELETE FROM games WHERE id = ?";
        try (Connection conn = DatabaseConnection.connect(dbPath);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Remove failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Searches for a game in the database by its ID.
     *
     * @param id     The ID of the game to search for
     * @param dbPath The file path to the SQLite database
     * @return The Game object if found, otherwise null
     */
     // Searches the repository for a game by its ID.
     // Long id: The ID of the game to search for
     public Game findGame(long id, String dbPath) {
         String sql = "SELECT * FROM games WHERE id = ?";
         try (Connection conn = DatabaseConnection.connect(dbPath);
              PreparedStatement stmt = conn.prepareStatement(sql)) {
             stmt.setLong(1, id);
             ResultSet rs = stmt.executeQuery();
             if (rs.next()) {
                 return new Game(
                         rs.getLong("id"),
                         rs.getString("title"),
                         rs.getString("genre"),
                         rs.getString("platform"),
                         rs.getBoolean("completed")
                 );
             }
         } catch (SQLException e) {
             System.out.println("Find failed: " + e.getMessage());
         }
         return null;
     }

    /**
     * Retrieves all games from the database.
     *
     * @param dbPath The file path to the SQLite database
     * @return A list of all Game objects stored in the database
     */
    // Retrieves the full list of games in the repository.
    public List<Game> getAllGames(String dbPath) {
        List<Game> games = new ArrayList<>();
        String sql = "SELECT * FROM games";
        try (Connection conn = DatabaseConnection.connect(dbPath);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                games.add(new Game(
                        rs.getLong("id"),
                        rs.getString("title"),
                        rs.getString("genre"),
                        rs.getString("platform"),
                        rs.getBoolean("completed")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Get all failed: " + e.getMessage());
        }
        return games;
    }

    /**
     * Updates an existing game in the database.
     *
     * @param game   The updated Game object
     * @param dbPath The file path to the SQLite database
     * @return true if the update was successful, false otherwise
     */
    // Updates games in the repository
    public boolean updateGame(Game game, String dbPath) {
        String sql = "UPDATE games SET title = ?, genre = ?, platform = ?, completed = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.connect(dbPath);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, game.getTitle());
            stmt.setString(2, game.getGenre());
            stmt.setString(3, game.getPlatform());
            stmt.setBoolean(4, game.isCompleted());
            stmt.setLong(5, game.getId());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Update failed: " + e.getMessage());
            return false;
        }
    }
}