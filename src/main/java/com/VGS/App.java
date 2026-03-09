package com.VGS;

import com.VGS.model.Game;
import com.VGS.repository.Gamerepository;
import com.VGS.service.GameService;

import java.util.List;
import java.util.Scanner;

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

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    public boolean run() {

        boolean running = true;

        while (running) {
            System.out.println("\n--- Game Collection Manager ---");
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

    private void viewGames() {
        List<Game> games = service.viewAllGames();
        if (games.isEmpty()) {
            System.out.println("No games found.");
            return;
        }
        for (Game game : games) System.out.println(game);
    }

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