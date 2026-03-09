package com.VGS.repository;

import com.VGS.model.Game;
import java.util.ArrayList;
import java.util.List;

public class Gamerepository {

    private List<Game> games = new ArrayList<>();

    public Game addGame(Game game) {
        games.add(game);
        return game;
    }

    public boolean removeGame(int id) {

        Game game = findGame(id);

        if (game != null) {
            games.remove(game);
            return true;
        }

        return false;
    }

    public Game findGame(int id) {

        for (Game game : games) {
            if (game.getId() == id) {
                return game;
            }
        }

        return null;
    }

    public List<Game> getAllGames() {
        return games;
    }
}