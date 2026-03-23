package com.VGS.service;

import com.VGS.model.Game;
import com.VGS.repository.Gamerepository;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class GameServiceTest {

    // The GameService instance I am testing
    private GameService service;

    /* Setup method runs before each test.
     * Initializes a new repository and GameService for isolation between tests.
     */
    @Before
    public void setUp() {
        Gamerepository repository = new Gamerepository();
        service = new GameService(repository);
    }

    /* Test adding a game to the service.
     * Verifies that addGame returns true and the game is actually added.
     */
    @Test
    public void testAddGame() {
        Game game = new Game(1, "Zelda", "Adventure", "Switch", false); // create test game
        boolean added = service.addGame(game); // attempt to add
        assertTrue(added);  // should return true

        // Verify that the game is in the list of all games
        List<Game> games = service.viewAllGames();
        assertEquals(1, games.size());  // only 1 game should exist
        assertEquals("Zelda", games.get(0).getTitle());   // title matches
    }

    /* Test removing a game that exists.
     * Ensures removeGame returns true and the game is removed from the list.
     */
    @Test
    public void testRemoveGame() {
        Game game = new Game(1, "Halo", "Shooter", "Xbox", false);
        service.addGame(game);

        boolean removed = service.removeGame(1); // attempt removal
        assertTrue(removed); // should succeed
        assertTrue(service.viewAllGames().isEmpty()); // list should be empty
    }

    /* Test removing a game that does not exist.
     * removeGame should return false.
     */
    @Test
    public void testRemoveGame_NotFound() {
        boolean removed = service.removeGame(99);  // non-existent ID
        assertFalse(removed); // should return false
    }

    /* Test updating an existing game's information.
     * updateGame returns boolean indicating success.
     * The actual fields of the game are verified using findGame().
     */
    @Test
    public void testUpdateGame() {
        Game game = new Game(1, "Old Title", "RPG", "PC", false);
        service.addGame(game);

        boolean updated = service.updateGame(1, "New Title", "Action", "PS5");  // boolean return (update info)
        assertTrue(updated); // check that update succeeded

        // check the actual updated object
        Game found = service.findGame(1);
        assertNotNull(found);   // game should exist
        assertEquals("New Title", found.getTitle());
        assertEquals("Action", found.getGenre());
        assertEquals("PS5", found.getPlatform());
    }

    /* Test updating a non-existent game.
     * Should return false since the game ID does not exist.
     */
    @Test
    public void testUpdateGame_NotFound() {
        boolean updated = service.updateGame(99, "Title", "Genre", "Platform"); // boolean
        assertFalse(updated);  // update fails
    }

    /* Test viewing all games in the service.
     * Adds two games and verifies that viewAllGames returns both.
     */
    @Test
    public void testViewAllGames() {
        service.addGame(new Game(1, "Game1", "RPG", "PC", false));
        service.addGame(new Game(2, "Game2", "Action", "PS5", false));

        List<Game> games = service.viewAllGames();
        assertEquals(2, games.size());
    }

    /* Test tracking completion of a game.
     * trackCompletion returns the updated Game object.
     * Verifies that the game's completed flag is set to true.
     */
    @Test
    public void testTrackCompletion() {
        Game game = new Game(1, "Minecraft", "Sandbox", "PC", false);
        service.addGame(game);

        Game completed = service.trackCompletion(1); // returns Game
        assertNotNull(completed); // game exists
        assertTrue(completed.isCompleted());  // completed should be true
    }

    /* Test tracking completion of a non-existent game.
     * Should return null since the ID does not exist.
     */
    @Test
    public void testTrackCompletion_NotFound() {
        Game completed = service.trackCompletion(99); // returns Game
        assertNull(completed);  // no game to complete
    }

    /* Test finding a game by ID.
     * Verifies that findGame returns the correct Game object.
     */
    @Test
    public void testFindGame() {
        Game game = new Game(1, "FIFA", "Sports", "PS5", false);
        service.addGame(game);

        Game found = service.findGame(1);
        assertNotNull(found);
        assertEquals("FIFA", found.getTitle());  // title matches
    }

    /* Test finding a non-existent game by ID.
     * Should return null since no game exists with this ID.
     */
    @Test
    public void testFindGame_NotFound() {
        Game found = service.findGame(99);
        assertNull(found);
    }

    /* Test loading games from a file.
     * Verifies that the number of loaded games matches the returned count.
     */
    @Test
    public void testLoadGamesFromFile() {
        int loadedCount = service.loadGamesFromFile("testGames.txt"); // returns int
        assertTrue(loadedCount >= 0);

        List<Game> games = service.viewAllGames();
        assertEquals(loadedCount, games.size()); // verify loaded count matches list
    }
}