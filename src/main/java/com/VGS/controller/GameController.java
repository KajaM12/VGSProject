package com.VGS.controller;

import com.VGS.model.Game;
import com.VGS.service.GameService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
// Marks this class as a Spring controller. It will handle web requests.
public class GameController {

    private final GameService service;
    // Reference to my service layer. Handles the logic like adding, removing, updating games.

    public GameController(GameService service) {
        this.service = service;
    }

    // View all games (Main Menu)
    @GetMapping("/")
    // Maps HTTP GET requests for "/" (root) to this method.
    public String viewGames(Model model) {
        // Passes the list of all games to the view (index.html) using the Model.
        model.addAttribute("games", service.viewAllGames());
        return "index";
        // Returns the name of the Thymeleaf template to render (index.html).
    }

    // Shows add game form
    @GetMapping("/add")
    public String showAddForm() {
        return "add-game";
    }

    // Adds game
    @PostMapping("/add")
    // Handles the form with adding a new game.
    public String addGame(@RequestParam String id,
                          @RequestParam String title,
                          @RequestParam String genre,
                          @RequestParam String platform,
                          Model model) {

        long gameId;

        // Validates ID: numeric and must be between 1-10 digits
        if (!id.matches("\\d{1,10}")) {
            model.addAttribute("errorMessage", "ID must be numeric and 1-10 digits.");
            model.addAttribute("games", service.viewAllGames());
            return "index";
        }

        try {
            gameId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            model.addAttribute("errorMessage", "Invalid ID.");
            model.addAttribute("games", service.viewAllGames());
            return "index";
        }

        // Checks for duplicate IDs
        if (service.existsById(gameId)) {
            model.addAttribute("errorMessage", "ID already exists.");
            model.addAttribute("games", service.viewAllGames());
            return "index";
        }

        // Validate genre: letters only
        if (!genre.matches("[a-zA-Z\\s]+")) {
            model.addAttribute("errorMessage", "Genre must contain letters only.");
            model.addAttribute("games", service.viewAllGames());
            return "index";
        }

        // Validate title
        if (title.isBlank()) {
            model.addAttribute("errorMessage", "Title is required.");
            model.addAttribute("games", service.viewAllGames());
            return "index";
        }

        // Validate platform
        if (platform.isBlank()) {
            model.addAttribute("errorMessage", "Platform is required.");
            model.addAttribute("games", service.viewAllGames());
            return "index";
        }

        // All validations passed, create a new Game object
        Game game = new Game(gameId, title, genre, platform, false);
        service.addGame(game);

        model.addAttribute("successMessage", "Game added successfully!");
        model.addAttribute("games", service.viewAllGames());
        return "index";
    }

    // Removes game
    @GetMapping("/delete/{id}")
    public String deleteGame(@PathVariable String id, Model model) {
        long gameId;
        try {
            gameId = Long.parseLong(id); // convert string to long
        } catch (NumberFormatException e) {
            model.addAttribute("errorMessage", "Invalid ID.");
            model.addAttribute("games", service.viewAllGames());
            return "index";
        }

        boolean removed = service.removeGame(gameId); // attempt to remove game
        if (!removed) {
            model.addAttribute("errorMessage", "Game not found.");
        } else {
            model.addAttribute("successMessage", "Game deleted successfully!");
        }

        model.addAttribute("games", service.viewAllGames()); // refresh main menu
        return "index";
    }

    // Marks complete
    @GetMapping("/complete/{id}")
    public String completeGame(@PathVariable String id, Model model) {
        long gameId;
        try {
            gameId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            model.addAttribute("errorMessage", "Invalid game ID: " + id);
            model.addAttribute("games", service.viewAllGames());
            return "index";
        }

        Game completedGame = service.trackCompletion(gameId); // mark as completed
        if (completedGame == null) {
            model.addAttribute("errorMessage", "Game with ID " + id + " not found.");
        } else {
            model.addAttribute("successMessage", "Game marked as completed!");
        }

        model.addAttribute("games", service.viewAllGames()); // refresh main menu
        return "index";
    }

    // Show update form
    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable String id, Model model) {
        try {
            long gameId = Long.parseLong(id);
            Game game = service.findGame(gameId);
            if (game == null) {
                model.addAttribute("errorMessage", "Game with ID " + id + " not found.");
                model.addAttribute("games", service.viewAllGames());
                return "index";
            }
            model.addAttribute("game", game); // pre-fill form
            return "update-game"; // update-game.html
        } catch (NumberFormatException e) {
            model.addAttribute("errorMessage", "Invalid game ID: " + id);
            model.addAttribute("games", service.viewAllGames());
            return "index";
        }
    }

    // Handle update POST
    @PostMapping("/update")
    public String updateGame(@RequestParam long id,
                             @RequestParam String title,
                             @RequestParam String genre,
                             @RequestParam String platform,
                             Model model) {

        // Validate fields
        if (title.isBlank() || genre.isBlank() || platform.isBlank()) {
            model.addAttribute("errorMessage", "All fields are required.");
            model.addAttribute("game", service.findGame(id));
            return "update-game";
        }

        if (!genre.matches("[a-zA-Z\\s]+")) {
            model.addAttribute("errorMessage", "Genre must contain letters only.");
            model.addAttribute("game", service.findGame(id));
            return "update-game";
        }

        boolean updated = service.updateGame(id, title, genre, platform);

        if (updated) {
            model.addAttribute("successMessage", "Game updated successfully!");
        } else {
            model.addAttribute("errorMessage", "Game not found.");
        }

        model.addAttribute("games", service.viewAllGames());
        return "index";
    }

    // Exits the page
    @GetMapping("/exit")
    public String exitPage() {
        return "exit"; // It will return exit.html
    }
}

