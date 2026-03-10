/**
 * Kaja Moore
 * CEN 3024C Software Development I
 * 03/08/2026
 * VideoGameCollection.java
 *
 * Class Name: Phase 1
 *
 * The Video Game Collection System allows users to manage their personal video game library.
 * It enables adding, removing, updating, viewing, and tracking completion of games, while
 * validating input and maintaining organized, accurate information of the collection.
 */

 // Video Link: https://youtu.be/3PdzgcXUtBs
 // GitHubLink: https://github.com/KajaM12/VGSProject.git
package com.VGS;

import com.VGS.model.Game;
import com.VGS.repository.Gamerepository;
import com.VGS.service.GameService;

import java.util.List;
import java.util.Scanner;


/* Constructor: App
 *
 * Initializes the GameService and Scanner for user input.
 * Loads the games from a text file into the collection.
 */
public class App {

    private GameService service;
    private Scanner scanner;

    public App() {
        Gamerepository repository = new Gamerepository();
        service = new GameService(repository);
        scanner = new Scanner(System.in);

        // Load games from the text file
        String fileName = "games.txt";
        int loaded = service.loadGamesFromFile(fileName);
        System.out.println(loaded + " games loaded from " + fileName);
    }

    /* Method: main
     *
     * The main program of the application.
     * Creates an instance of the App and starts the program loop.
     */
    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    /* Method: run
     *
     * Handles the main program loop for the user interface.
     * Displays the menu, gets user input, and calls the
     * appropriate methods based on the option selected.
     *
     * it will return: boolean - returns whether the program is still running
     */
    public boolean run() {

        boolean running = true;

        while (running) {
            System.out.println("\n--- Video Game Collection System ---");
            System.out.println("1. Add Game");
            System.out.println("2. Remove Game");
            System.out.println("3. Update Game");
            System.out.println("4. View All Games");
            System.out.println("5. Track Completion");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");

            int option;

            while (true) {
                String input = scanner.nextLine();
                try {
                    option = Integer.parseInt(input);
                    if (option >= 1 && option <= 6) break;
                    System.out.println("Invalid option. Enter a number 1-6:");
                } catch (NumberFormatException e) {
                    System.out.println("Invalid option. Enter a number 1-6:");
                }
            }

            switch (option) {
                case 1 -> addGame();
                case 2 -> removeGame();
                case 3 -> updateGame();
                case 4 -> viewGames();
                case 5 -> trackCompletion();
                case 6 -> {
                    running = false;
                    System.out.println("Goodbye!");
                }
            }
        }

        return running;
    }

    /* Method: addGame
     *
     * Adds a new game to the collection. Validates the game ID,
     * title, genre, and platform before adding. Ensures no duplicate IDs.
     *
     */
    private void addGame() {

        long id;

        while (true) {
            System.out.println("Enter Game ID (max 10 digits):");

            try {
                id = Long.parseLong(scanner.nextLine());

                if (id <= 0 || String.valueOf(id).length() > 10) {
                    System.out.println("Invalid ID. Must be 1–10 digits.");
                    continue;
                }

                if (service.findGame(id) != null) {
                    System.out.println("A game with this ID already exists.");
                    continue;
                }

                break;

            } catch (NumberFormatException e) {
                System.out.println("Invalid ID. Must be numeric.");
            }
        }

        System.out.println("Enter Title:");
        String title = scanner.nextLine();

        String genre;
        while (true) {
            System.out.println("Enter Genre (letters only):");
            genre = scanner.nextLine();

            if (!genre.matches("[a-zA-Z ]+")) {
                System.out.println("Invalid genre. Letters only.");
                continue;
            }
            break;
        }

        System.out.println("Enter Platform:");
        String platform = scanner.nextLine();

        Game game = new Game(id, title, genre, platform, false);

        if (service.addGame(game))
            System.out.println("Game added successfully!");
    }

    /* Method: removeGame
     *
     * Removes a game from the collection by its ID.
     * Handles invalid or non-existing IDs carefully.
     *
     */
    private void removeGame() {
        try {
            System.out.println("Enter Game ID to remove:");
            long id = Long.parseLong(scanner.nextLine());

            boolean removed = service.removeGame(id);
            if (removed) System.out.println("Game removed.");
            else System.out.println("Game not found. Returning to main menu.");

        } catch (NumberFormatException e) {
            System.out.println("Invalid ID. Must be numeric. Returning to main menu.");
        } catch (Exception e) {
            System.out.println("An error occurred. Returning to main menu.");
        }
    }

    /* Method: updateGame
     *
     * Updates the details of an existing game, including title,
     * genre, and platform. Validates the input before updating.
     *
     */
    private void updateGame() {
        try {
            System.out.println("Enter Game ID to update:");
            long id = Long.parseLong(scanner.nextLine());

            Game game = service.findGame(id);
            if (game == null) {
                System.out.println("Game not found. Returning to main menu.");
                return;
            }

            System.out.println("New Title:");
            String title = scanner.nextLine();
            System.out.println("New Genre (letters only):");
            String genre = scanner.nextLine();
            System.out.println("New Platform:");
            String platform = scanner.nextLine();

            if (title.isBlank() || genre.isBlank() || platform.isBlank()) {
                System.out.println("All fields are required. Returning to main menu.");
                return;
            }

            if (!genre.matches("[a-zA-Z ]+")) {
                System.out.println("Invalid genre. Only letters allowed. Returning to main menu.");
                return;
            }

            if (service.updateGame(id, title, genre, platform)) {
                System.out.println("Game updated successfully!");
            } else {
                System.out.println("Failed to update game.");
            }

        } catch (NumberFormatException e) {
            System.out.println("Invalid ID. Must be numeric. Returning to main menu.");
        } catch (Exception e) {
            System.out.println("An error occurred. Returning to main menu.");
        }
    }

    /* Method: viewGames
     *
     * Displays all games currently stored in the collection.
     */
    private void viewGames() {
        List<Game> games = service.viewAllGames();
        if (games.isEmpty()) {
            System.out.println("No games found.");
            return;
        }
        for (Game game : games) System.out.println(game);
    }

    /* Method: trackCompletion (Custom Main Method)
     *
     * Marks a game as completed based on its ID.
     * Validates input and handles non-existent games.
     *
     */
    private void trackCompletion() {
        try {
            System.out.println("Enter Game ID to mark completed:");
            long id = Long.parseLong(scanner.nextLine());

            Game game = service.trackCompletion(id);
            if (game == null) System.out.println("Game not found. Returning to main menu.");
            else System.out.println("Game marked as completed.");

        } catch (NumberFormatException e) {
            System.out.println("Invalid ID. Must be numeric. Returning to main menu.");
        } catch (Exception e) {
            System.out.println("An error occurred. Returning to main menu.");
        }
    }
}