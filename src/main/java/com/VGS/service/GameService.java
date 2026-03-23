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
public class GameService {

    private Gamerepository repository;

   // Initializes the service with a given game repository.
    public GameService(Gamerepository repository) {

        this.repository = repository;
    }

    // Adds a new game to the repository.
    public boolean addGame(Game game) {

        return repository.addGame(game);
    }

    // Remove a game by ID.
    public boolean removeGame(long id) {

        return repository.removeGame(id);
    }

    // View all games from list.
    public List<Game> viewAllGames() {

        return repository.getAllGames();
    }

    // Update existing game from list.
    public boolean updateGame(long id, String title, String genre, String platform) {
        Game game = repository.findGame(id);
        if (game == null) return false;

        game.setTitle(title);
        game.setGenre(genre);
        game.setPlatform(platform);

        return true;
    }

    // Track completion (true or false).
    public Game trackCompletion(long id) {
        Game game = repository.findGame(id);
        if (game != null) {
            game.setCompleted(true);
        }
        return game;
    }

    // Find game by ID.
    public Game findGame(long id) {
        return repository.findGame(id);
    }

    // Loads games from a text file in the resources folder.
    // Skips invalid lines or improperly formatted data.
    public int loadGamesFromFile(String fileName) {
        int count = 0;
        Scanner fileScanner = null;

        try {
            // Load as a resource from the JAR
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

                    if (title.isBlank() || !genre.matches("[a-zA-Z ]+") || platform.isBlank() || id <= 0) {
                        continue;
                    }

                    Game game = new Game(id, title, genre, platform, completed);
                    if (repository.addGame(game)) count++;

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