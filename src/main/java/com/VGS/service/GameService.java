package com.VGS.service;

import com.VGS.App;
import com.VGS.model.Game;
import com.VGS.repository.Gamerepository;

import java.io.File;
import java.util.List;
import java.util.Scanner;

public class GameService {

    private Gamerepository repository;

    public GameService(Gamerepository repository) {
        this.repository = repository;
    }

    // Add a game safely
    public boolean addGame(Game game) {
        return repository.addGame(game);
    }

    // Remove a game by ID
    public boolean removeGame(long id) {
        return repository.removeGame(id);
    }

    // View all games
    public List<Game> viewAllGames() {
        return repository.getAllGames();
    }

    // Update existing game
    public boolean updateGame(long id, String title, String genre, String platform) {
        Game game = repository.findGame(id);
        if (game == null) return false;

        game.setTitle(title);
        game.setGenre(genre);
        game.setPlatform(platform);

        return true;
    }

    // Track completion
    public Game trackCompletion(long id) {
        Game game = repository.findGame(id);
        if (game != null) {
            game.setCompleted(true);
        }
        return game;
    }

    // Find game by ID
    public Game findGame(long id) {
        return repository.findGame(id);
    }

    // Load games from file
    public int loadGamesFromFile(String fileName) {
        int count = 0;
        Scanner fileScanner = null;

        try {
            File file = new File(fileName);

            if (file.exists()) {
                fileScanner = new Scanner(file);
            } else if (App.class.getClassLoader().getResource(fileName) != null) {
                fileScanner = new Scanner(App.class.getClassLoader().getResourceAsStream(fileName));
            } else {
                System.out.println("File not found: " + fileName);
                return 0;
            }

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

                    if (title.isBlank()) continue;
                    if (!genre.matches("[a-zA-Z ]+")) continue;
                    if (platform.isBlank()) continue;
                    if (id <= 0) continue;

                    Game game = new Game(id, title, genre, platform, completed);

                    if (repository.addGame(game)) {
                        count++;
                    }

                } catch (Exception ignored) {}

            }

        } catch (Exception e) {
            System.out.println("Error loading file: " + e.getMessage());
        } finally {
            if (fileScanner != null) fileScanner.close();
        }

        return count;
    }
}