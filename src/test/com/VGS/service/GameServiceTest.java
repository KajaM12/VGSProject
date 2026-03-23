package com.VGS.service;

import com.VGS.model.Game;
import com.VGS.repository.Gamerepository;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class GameServiceTest {

    private GameService service;

    @Before
    public void setUp() {
        Gamerepository repository = new Gamerepository();
        service = new GameService(repository);
    }

    @Test
    public void testAddGame() {
        Game game = new Game(1, "Zelda", "Adventure", "Switch", false);
        boolean added = service.addGame(game);
        assertTrue(added);

        List<Game> games = service.viewAllGames();
        assertEquals(1, games.size());
        assertEquals("Zelda", games.get(0).getTitle());
    }

    @Test
    public void testRemoveGame() {
        Game game = new Game(1, "Halo", "Shooter", "Xbox", false);
        service.addGame(game);

        boolean removed = service.removeGame(1);
        assertTrue(removed);
        assertTrue(service.viewAllGames().isEmpty());
    }

    @Test
    public void testRemoveGame_NotFound() {
        boolean removed = service.removeGame(99);
        assertFalse(removed);
    }

    /*
    Test: Update Game
    */
    @Test
    public void testUpdateGame() {
        Game game = new Game(1, "Old Title", "RPG", "PC", false);
        service.addGame(game);

        boolean updated = service.updateGame(1, "New Title", "Action", "PS5");  // boolean return
        assertTrue(updated); // check that update succeeded

        // check the actual updated object
        Game found = service.findGame(1);
        assertNotNull(found);
        assertEquals("New Title", found.getTitle());
        assertEquals("Action", found.getGenre());
        assertEquals("PS5", found.getPlatform());
    }

    /*
    Test: Update Game (Not Found)
    */
    @Test
    public void testUpdateGame_NotFound() {
        boolean updated = service.updateGame(99, "Title", "Genre", "Platform"); // boolean
        assertFalse(updated);
    }


    @Test
    public void testViewAllGames() {
        service.addGame(new Game(1, "Game1", "RPG", "PC", false));
        service.addGame(new Game(2, "Game2", "Action", "PS5", false));

        List<Game> games = service.viewAllGames();
        assertEquals(2, games.size());
    }

    @Test
    public void testTrackCompletion() {
        Game game = new Game(1, "Minecraft", "Sandbox", "PC", false);
        service.addGame(game);

        Game completed = service.trackCompletion(1); // returns Game
        assertNotNull(completed);
        assertTrue(completed.isCompleted());
    }

    @Test
    public void testTrackCompletion_NotFound() {
        Game completed = service.trackCompletion(99); // returns Game
        assertNull(completed);
    }

    @Test
    public void testFindGame() {
        Game game = new Game(1, "FIFA", "Sports", "PS5", false);
        service.addGame(game);

        Game found = service.findGame(1);
        assertNotNull(found);
        assertEquals("FIFA", found.getTitle());
    }

    @Test
    public void testFindGame_NotFound() {
        Game found = service.findGame(99);
        assertNull(found);
    }

    @Test
    public void testLoadGamesFromFile() {
        int loadedCount = service.loadGamesFromFile("testGames.txt"); // returns int
        assertTrue(loadedCount >= 0);

        List<Game> games = service.viewAllGames();
        assertEquals(loadedCount, games.size());
    }
}