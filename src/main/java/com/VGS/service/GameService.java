package com.VGS.service;

import com.VGS.model.Game;
import com.VGS.repository.Gamerepository;

import java.util.List;
import java.util.Scanner;
import java.io.File;

public class GameService {

    private Gamerepository repository;

    public GameService(Gamerepository repository) {
        this.repository = repository;
    }

    public Game addGame(Game game) {
        return repository.addGame(game);
    }

    public boolean removeGame(int id) {
        return repository.removeGame(id);
    }

    public List<Game> viewAllGames() {
        return repository.getAllGames();
    }

    public Game updateGame(int id, String title, String genre, String platform) {

        Game game = repository.findGame(id);

        if (game == null) {
            return null;
        }

        Game updatedGame = new Game(
                id,
                title,
                genre,
                platform,
                game.isCompleted()
        );

        repository.removeGame(id);
        repository.addGame(updatedGame);

        return updatedGame;
    }

    public Game trackCompletion(int id) {

        Game game = repository.findGame(id);

        if (game != null) {
            game.setCompleted(true);
        }

        return game;
    }

    /*
    Loads games from a text file and adds them to the repository
    */

    public int loadGamesFromFile(String fileName) {

        int count = 0;

        try {

            Scanner fileScanner = new Scanner(new File(fileName));

            while (fileScanner.hasNextLine()) {

                String line = fileScanner.nextLine();
                String[] parts = line.split(",");

                int id = Integer.parseInt(parts[0]);
                String title = parts[1];
                String genre = parts[2];
                String platform = parts[3];
                boolean completed = Boolean.parseBoolean(parts[4]);

                Game game = new Game(id, title, genre, platform, completed);

                repository.addGame(game);

                count++;
            }

            fileScanner.close();

        } catch (Exception e) {
            System.out.println("Error loading file: " + e.getMessage());
        }

        return count;
    }
}

