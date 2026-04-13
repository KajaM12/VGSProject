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
    public String viewGames(Model model) {
        model.addAttribute("games", service.viewAllGames());
        return "index";
    }

    // Shows add game form
    @GetMapping("/add")
    public String showAddForm() {
        return "add-game";
    }

    // Adds game
    @PostMapping("/add")
    public String addGame(@RequestParam String id,
                          @RequestParam String title,
                          @RequestParam String genre,
                          @RequestParam String platform,
                          Model model) {

        long gameId;

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

        if (service.existsById(gameId)) {
            model.addAttribute("errorMessage", "ID already exists.");
            model.addAttribute("games", service.viewAllGames());
            return "index";
        }

        if (!genre.matches("[a-zA-Z\\s]+")) {
            model.addAttribute("errorMessage", "Genre must contain letters only.");
            model.addAttribute("games", service.viewAllGames());
            return "index";
        }

        if (title.isBlank()) {
            model.addAttribute("errorMessage", "Title is required.");
            model.addAttribute("games", service.viewAllGames());
            return "index";
        }

        if (platform.isBlank()) {
            model.addAttribute("errorMessage", "Platform is required.");
            model.addAttribute("games", service.viewAllGames());
            return "index";
        }

        Game game = new Game(gameId, title, genre, platform, false);
        service.addGame(game);

        model.addAttribute("successMessage", "Game added successfully!");
        model.addAttribute("games", service.viewAllGames());
        return "index";
    }

    // DELETE (Selection-Based)
    @PostMapping("/delete")
    public String deleteGame(@RequestParam(required = false) Long selectedId, Model model) {

        if (selectedId == null) {
            model.addAttribute("errorMessage", "Please select a game to delete.");
            model.addAttribute("games", service.viewAllGames());
            return "index";
        }

        boolean removed = service.removeGame(selectedId);

        if (!removed) {
            model.addAttribute("errorMessage", "Game not found.");
        } else {
            model.addAttribute("successMessage", "Game deleted successfully!");
        }

        model.addAttribute("games", service.viewAllGames());
        return "index";
    }

    // COMPLETE (Selection-Based)
    @PostMapping("/complete")
    public String completeGame(@RequestParam(required = false) Long selectedId, Model model) {

        if (selectedId == null) {
            model.addAttribute("errorMessage", "Please select a game to mark complete.");
            model.addAttribute("games", service.viewAllGames());
            return "index";
        }

        Game completedGame = service.trackCompletion(selectedId);

        if (completedGame == null) {
            model.addAttribute("errorMessage", "Game not found.");
        } else {
            model.addAttribute("successMessage", "Game marked as completed!");
        }

        model.addAttribute("games", service.viewAllGames());
        return "index";
    }

    // SHOW UPDATE FORM (Selection-Based)
    @PostMapping("/show-update")
    public String showUpdateForm(@RequestParam(required = false) Long selectedId, Model model) {

        if (selectedId == null) {
            model.addAttribute("errorMessage", "Please select a game to update.");
            model.addAttribute("games", service.viewAllGames());
            return "index";
        }

        Game game = service.findGame(selectedId);

        if (game == null) {
            model.addAttribute("errorMessage", "Game not found.");
            model.addAttribute("games", service.viewAllGames());
            return "index";
        }

        model.addAttribute("game", game);
        return "update-game";
    }

    // Handle update POST
    @PostMapping("/update")
    public String updateGame(@RequestParam long id,
                             @RequestParam String title,
                             @RequestParam String genre,
                             @RequestParam String platform,
                             @RequestParam(required = false) String completed,
                             Model model) {

        boolean isCompleted = (completed != null);

        Game existing = service.findGame(id);
        if (existing == null) {
            model.addAttribute("errorMessage", "Game not found.");
            return "index";
        }

        Game updatedGame = new Game(id, title, genre, platform, isCompleted);

        boolean updated = service.updateGame(id, title, genre, platform);

        if (updated) {
            model.addAttribute("successMessage", "Game updated successfully!");
        } else {
            model.addAttribute("errorMessage", "Update failed.");
        }

        model.addAttribute("games", service.viewAllGames());
        return "index";
    }

    // Exits the page
    @GetMapping("/exit")
    public String exitPage() {
        return "exit";
    }
}

