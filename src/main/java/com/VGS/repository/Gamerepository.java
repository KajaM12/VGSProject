package com.VGS.repository;

import com.VGS.model.Game;
import java.util.ArrayList;
import java.util.List;

public class Gamerepository {

    private List<Game> games = new ArrayList<>();


     // Adds a new game to the repository if it is not null and has a unique ID.
     // Game game: The Game object to add.
     // Return: boolean - true if the game was added, false if null or duplicate ID.
    public boolean addGame(Game game) {
        if (game == null || findGame(game.getId()) != null) {
            // Null game or duplicate ID
            return false;
        }
        games.add(game);
        return true;
    }

    // Removes a game from the repository based on its ID.
    // Long id: The ID of the game to remove
    public boolean removeGame(long id) {
        Game game = findGame(id);
        if (game != null) {
            games.remove(game);
            return true;
        }
        return false;
    }

     // Searches the repository for a game by its ID.
     // Long id: The ID of the game to search for
    public Game findGame(long id) {
        for (Game game : games) {
            if (game.getId() == id) return game;
        }
        return null;
    }

    // Retrieves the full list of games in the repository.
    public List<Game> getAllGames() {
        return games;
    }
}