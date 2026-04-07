package com.VGS.service;

import com.VGS.model.Game;
import com.VGS.repository.Gamerepository;
import org.junit.Before;
import org.junit.Test;
import java.sql.Connection;
import java.sql.Statement;
import com.VGS.config.DatabaseConnection;

import java.util.List;

import static org.junit.Assert.*;

//Updated our GameServiceTest to make sure the GameService class runs correctly.
public class GameServiceTest {

    // The GameService instance I am testing + the DB Path
    private GameService service;
    private final String testDbPath = "games.db";

    /* Setup method runs before each test.
     * Initializes a new repository and GameService for isolation between tests.
     */
    @Before
    public void setUp() {
        Gamerepository repository = new Gamerepository();

        // Clear database before each test to prevent duplicate ID issues
        try (Connection conn = DatabaseConnection.connect(testDbPath);
             Statement stmt = conn.createStatement()) {

            stmt.execute("""
            CREATE TABLE IF NOT EXISTS games (
                id INTEGER PRIMARY KEY,
                title TEXT NOT NULL,
                genre TEXT NOT NULL,
                platform TEXT NOT NULL,
                completed BOOLEAN
            );
        """);

            stmt.execute("DELETE FROM games");

        } catch (Exception e) {
            System.out.println("Failed to reset DB: " + e.getMessage());
        }

        service = new GameService(repository, testDbPath);
    }

    /* Test adding a game to the service.
     * Verifies that addGame returns true and the game is actually added.
     */
    @Test
    public void testAddGame() {
        Game game = new Game(1, "Zelda", "Adventure", "Switch", false);
        boolean added = service.addGame(game);
        assertTrue("Game should be added successfully", added);

        // Attempt duplicate ID
        Game duplicate = new Game(1, "Mario", "Platformer", "Switch", false);
        boolean duplicateAdded = service.addGame(duplicate);
        assertFalse("Duplicate ID should not be added", duplicateAdded);
    }

    /* Test removing a game that exists.
     * Ensures removeGame returns true and the game is removed from the list.
     */
    @Test
    public void testRemoveGame() {
        service.addGame(new Game(4, "Halo", "FPS", "Xbox", false));
        boolean removed = service.removeGame(4);
        assertTrue("Game should be removed", removed);

        // Attempt to remove non-existing game
        boolean removedAgain = service.removeGame(999);
        assertFalse("Removing non-existing game should fail", removedAgain);
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
        service.addGame(new Game(5, "FIFA", "Sports", "PS5", false));

        boolean updated = service.updateGame(5, "FIFA 24", "Sports", "PS5");
        assertTrue("Game should be updated successfully", updated);

        Game updatedGame = service.findGame(5);
        assertEquals("Title should be updated", "FIFA 24", updatedGame.getTitle());

    /* Test updating a non-existent game.
     * Should return false since the game ID does not exist.
     */
    boolean updateNonExistent = service.updateGame(999, "Unknown", "None", "None");
        assertFalse("Updating non-existing game should fail", updateNonExistent);
    }

    /* Test viewing all games in the service.
     * Adds two games and verifies that viewAllGames returns both.
     */
    @Test
    public void testViewAllGames() {
        service.addGame(new Game(2, "Mario Kart", "Racing", "Switch", false));
        service.addGame(new Game(3, "Minecraft", "Sandbox", "PC", false));

        List<Game> games = service.viewAllGames();
        assertEquals("Two games should be loaded", 2, games.size());
    }

    /* Test tracking completion of a game.
     * trackCompletion returns the updated Game object.
     * Verifies that the game's completed flag is set to true.
     */
    @Test
    public void testTrackCompletion() {
        service.addGame(new Game(6, "Overwatch", "FPS", "PC", false));

        Game completed = service.trackCompletion(6);
        assertNotNull("Game should exist", completed);
        assertTrue("Game should be marked as completed", completed.isCompleted());

        // Attempt to complete non-existing game
        Game nonExistent = service.trackCompletion(999);
        assertNull("Non-existing game should return null", nonExistent);
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
        int loadedCount = service.loadGamesFromFile("games.txt"); // returns int
        assertTrue(loadedCount >= 0);

        List<Game> games = service.viewAllGames();
        assertEquals(loadedCount, games.size()); // verify loaded count matches list
    }
}