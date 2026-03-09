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

        int loaded = service.loadGamesFromFile("games.txt");
        System.out.println(loaded + " games loaded.");
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

            int option = Integer.parseInt(scanner.nextLine());

            switch (option) {

                case 1:
                    addGame();
                    break;

                case 2:
                    removeGame();
                    break;

                case 3:
                    updateGame();
                    break;

                case 4:
                    viewGames();
                    break;

                case 5:
                    trackCompletion();
                    break;

                case 6:
                    running = false;
                    System.out.println("Goodbye!");
                    break;

                default:
                    System.out.println("Invalid option.");
            }
        }

        return running;
    }

    private void addGame() {

        System.out.println("Enter Game ID:");
        int id = Integer.parseInt(scanner.nextLine());

        System.out.println("Enter Title:");
        String title = scanner.nextLine();

        System.out.println("Enter Genre:");
        String genre = scanner.nextLine();

        System.out.println("Enter Platform:");
        String platform = scanner.nextLine();

        Game game = new Game(id, title, genre, platform, false);

        service.addGame(game);

        System.out.println("Game added successfully!");
    }

    private void removeGame() {

        System.out.println("Enter Game ID to remove:");
        int id = Integer.parseInt(scanner.nextLine());

        boolean removed = service.removeGame(id);

        if (removed) {
            System.out.println("Game removed.");
        } else {
            System.out.println("Game not found.");
        }
    }

    private void updateGame() {

        System.out.println("Enter Game ID to update:");
        int id = Integer.parseInt(scanner.nextLine());

        System.out.println("New Title:");
        String title = scanner.nextLine();

        System.out.println("New Genre:");
        String genre = scanner.nextLine();

        System.out.println("New Platform:");
        String platform = scanner.nextLine();

        Game game = service.updateGame(id, title, genre, platform);

        if (game == null) {
            System.out.println("Game not found.");
        } else {
            System.out.println("Game updated.");
        }
    }

    private void viewGames() {

        List<Game> games = service.viewAllGames();

        if (games.isEmpty()) {
            System.out.println("No games found.");
            return;
        }

        for (Game game : games) {
            System.out.println(game);
        }
    }

    private void trackCompletion() {

        System.out.println("Enter Game ID to mark completed:");
        int id = Integer.parseInt(scanner.nextLine());

        Game game = service.trackCompletion(id);

        if (game == null) {
            System.out.println("Game not found.");
        } else {
            System.out.println("Game marked as completed.");
        }
    }
}