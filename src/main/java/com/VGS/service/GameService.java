package com.VGS.service;

import com.VGS.App;
import com.VGS.model.Game;
import com.VGS.repository.Gamerepository;

import java.io.File;
import java.util.List;
import java.util.Scanner;

/* Provides the business logic for the Video Game Collection System.
 * Acts as a service layer between the user interface (App) and the repository (Gamerepository).
 * Handles adding, removing, updating, viewing, and marking games as completed.
 * Also loads games safely from a text file.
 */

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GameService {

    private final Gamerepository repository;
    private final String dbPath;

    // Added dbPath in order for the database to connect smoothly through the GUI
    public GameService(Gamerepository repository, String dbPath) {
        this.repository = repository;
        this.dbPath = dbPath;
    }

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

    // Remove a game by ID
    public boolean removeGame(long id) {
        try {
            return repository.removeGame(id, dbPath);
        } catch (Exception e) {
            System.out.println("Remove failed: " + e.getMessage());
            return false;
        }
    }

    // View all games
    public List<Game> viewAllGames() {
        try {
            return repository.getAllGames(dbPath);
        } catch (Exception e) {
            System.out.println("Load failed: " + e.getMessage());
            return List.of(); // empty list if error
        }
    }

    // Check if a game exists by ID
    public boolean existsById(long id) {
        return findGame(id) != null;
    }

    // Update a game by ID
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

    // Mark a game as completed
    public Game trackCompletion(long id) {
        Game game = findGame(id);
        if (game == null) return null;

        game.setCompleted(true);
        boolean updated = updateGame(game.getId(), game.getTitle(), game.getGenre(), game.getPlatform());
        return updated ? game : null;
    }

    // Find a game by ID
    public Game findGame(long id) {
        try {
            return repository.findGame(id, dbPath);
        } catch (Exception e) {
            System.out.println("Find failed: " + e.getMessage());
            return null;
        }
    }

    /* Load games from a text file directly into the SQLite database.
     * Each line must be in format: id,title,genre,platform,completed
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