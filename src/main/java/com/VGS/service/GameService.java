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
        return repository.addGame(game, dbPath);
    }

    // Remove a game by ID
    public boolean removeGame(long id) {
        return repository.removeGame(id, dbPath);
    }

    // View all games
    public List<Game> viewAllGames() {
        return repository.getAllGames(dbPath);
    }

    // Check if a game exists by ID
    public boolean existsById(long id) {
        return repository.getAllGames(dbPath).stream()
                .anyMatch(game -> game.getId() == id);
    }

    // Update a game by ID
    public boolean updateGame(long id, String title, String genre, String platform) {
        Game existingGame = repository.findGame(id, dbPath);
        if (existingGame == null) return false;

        Game updatedGame = new Game(id, title, genre, platform, existingGame.isCompleted());
        return repository.updateGame(updatedGame, dbPath);
    }

    // Mark a game as completed
    public Game trackCompletion(long id) {
        Game game = repository.findGame(id, dbPath);
        if (game != null) {
            game.setCompleted(true);
            repository.updateGame(game, dbPath);
        }
        return game;
    }

    // Find a game by ID
    public Game findGame(long id) {
        return repository.findGame(id, dbPath);
    }

    /**
     * Load games from a text file directly into the SQLite database.
     * Each line must be in format: id,title,genre,platform,completed
     */
    public int loadGamesFromFile(String fileName) {
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

                    // Validate fields
                    if (title.isBlank() || !genre.matches("[a-zA-Z ]+") || platform.isBlank() || id <= 0) {
                        continue;
                    }

                    Game game = new Game(id, title, genre, platform, completed);
                    if (repository.addGame(game, dbPath)) count++;

                } catch (Exception e) {
                    // skip invalid line
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