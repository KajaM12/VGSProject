package com.VGS.repository;

import com.VGS.model.Game;
import java.util.ArrayList;
import java.util.List;

public class Gamerepository {

    private List<Game> games = new ArrayList<>();

    // Add game only if ID is unique
    public boolean addGame(Game game) {
        if (game == null || findGame(game.getId()) != null) {
            // Null game or duplicate ID
            return false;
        }
        games.add(game);
        return true;
    }

    public boolean removeGame(long id) {
        Game game = findGame(id);
        if (game != null) {
            games.remove(game);
            return true;
        }
        return false;
    }

    public Game findGame(long id) {
        for (Game game : games) {
            if (game.getId() == id) return game;
        }
        return null;
    }

    public List<Game> getAllGames() {
        return games;
    }
}