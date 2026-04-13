package com.VGS.service;

import com.VGS.App;
import com.VGS.model.Game;
import com.VGS.repository.Gamerepository;

import java.io.File;
import java.util.List;
import java.util.Scanner;

/**
 * Provides the business logic for the Video Game Collection System.
 * Acts as a service layer between the user interface (App) and the repository (Gamerepository).
 * Handles adding, removing, updating, viewing, and marking games as completed.
 * Also loads games safely from a text file into the SQLite database.
 */

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GameService {

    private final Gamerepository repository;
    private final String dbPath;

    /**
     * Constructor for GameService.
     *
     * @param repository The repository used for database operations.
     * @param dbPath The file path to the SQLite database.
     */
    // Added dbPath in order for the database to connect smoothly through the GUI
    public GameService(Gamerepository repository, String dbPath) {
        this.repository = repository;
        this.dbPath = dbPath;
    }

    /**
     * Adds a new game to the database.
     * Prevents adding null games or duplicate IDs.
     *
     * @param game The Game object to add.
     * @return true if added successfully, false otherwise.
     */
    // Add a game to SQLite
    public boolean addGame(Game game) {
        if (game == null || existsById(game.getId())) {
            return false; // prevents null or duplicate ID
        }
        try {
            return repository.addGame(game, dbPath);
        } catch (Exception e) {
            System.out.println("Add failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Removes a game from the database by ID.
     *
     * @param id The ID of the game to remove.
     * @return true if removed successfully, false otherwise.
     */
    // Remove a game by ID
    public boolean removeGame(long id) {
        try {
            return repository.removeGame(id, dbPath);
        } catch (Exception e) {
            System.out.println("Remove failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves all games from the database.
     *
     * @return A list of all Game objects, or an empty list if an error occurs.
     */
    public List<Game> viewAllGames() {
        try {
            return repository.getAllGames(dbPath);
        } catch (Exception e) {
            System.out.println("Load failed: " + e.getMessage());
            return List.of(); // empty list if error
        }
    }

    /**
     * Checks if a game exists in the database by ID.
     *
     * @param id The ID to check.
     * @return true if the game exists, false otherwise.
     */
    public boolean existsById(long id) {
        return findGame(id) != null;
    }

    /**
     * Updates an existing game's details.
     *
     * @param id The ID of the game to update.
     * @param title The new title.
     * @param genre The new genre.
     * @param platform The new platform.
     * @return true if update was successful, false if game not found or error occurs.
     */
    public boolean updateGame(long id, String title, String genre, String platform) {
        Game game = findGame(id);
        if (game == null) return false; // game not found

        game.setTitle(title);
        game.setGenre(genre);
        game.setPlatform(platform);

        try {
            return repository.updateGame(game, dbPath);
        } catch (Exception e) {
            System.out.println("Update failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Marks a game as completed in the database.
     *
     * @param id The ID of the game to mark as completed.
     * @return The updated Game object if successful, null otherwise.
     */
    public Game trackCompletion(long id) {
        // Find the game in the database
        Game game = repository.findGame(id, dbPath);

        if (game != null) {
            // Mark as completed
            game.setCompleted(true);

            // Update the database row
            boolean updated = repository.updateGame(game, dbPath);

            if (updated) {
                return game; // Return updated game
            } else {
                System.out.println("Failed to update game completion in DB for ID: " + id);
            }
        }
        return null; // game not found or DB update failed
    }

    /**
     * Finds a game by its ID.
     *
     * @param id The ID of the game.
     * @return The Game object if found, null otherwise.
     */
    public Game findGame(long id) {
        try {
            return repository.findGame(id, dbPath);
        } catch (Exception e) {
            System.out.println("Find failed: " + e.getMessage());
            return null;
        }
    }

    /**
     * Loads games from a text file into the SQLite database.
     * Skips invalid lines and prevents duplicate loading if database already contains data.
     *
     * File format: id,title,genre,platform,completed
     *
     * @param fileName The name of the file located in resources.
     * @return The number of games successfully loaded.
     */
    public int loadGamesFromFile(String fileName) {

        // Updated*** to check if DB already has data
        if (!repository.getAllGames(dbPath).isEmpty()) {
            System.out.println("Database already contains data. Skipping file load.");
            return 0;
        }

        int count = 0;
        Scanner fileScanner = null;

        try {
            var inputStream = App.class.getClassLoader().getResourceAsStream(fileName);
            if (inputStream == null) {
                System.out.println("File not found in resources: " + fileName);
                return 0;
            }

            fileScanner = new Scanner(inputStream);

            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split(",");

                if (parts.length < 5) continue;

                try {
                    long id = Long.parseLong(parts[0].trim());
                    String title = parts[1].trim();
                    String genre = parts[2].trim();
                    String platform = parts[3].trim();
                    boolean completed = Boolean.parseBoolean(parts[4].trim());

                    Game game = new Game(id, title, genre, platform, completed);

                    if (repository.addGame(game, dbPath)) count++;

                } catch (Exception e) {
                    // skip bad line
                }
            }

        } catch (Exception e) {
            System.out.println("Error loading file: " + e.getMessage());
        } finally {
            if (fileScanner != null) fileScanner.close();
        }

        return count;
    }
}