package com.VGS.model;

public class Game {
    private long id;
    private String title;
    private String genre;
    private String platform;
    private boolean completed;

    /**
     * Constructs a new Game object with the given attributes.
     * Validates input to ensure:
     * - ID is positive
     * - Title and platform are not null or blank
     * - Genre contains only letters
     *
     * @param id Unique identifier for the game (must be positive)
     * @param title Name of the game (cannot be null or blank)
     * @param genre Game genre (letters only)
     * @param platform Platform for the game (cannot be null or blank)
     * @param completed Whether the game has been completed
     * @throws IllegalArgumentException if any input is invalid
     */
    public Game(long id, String title, String genre, String platform, boolean completed) {
        if (id <= 0) throw new IllegalArgumentException("ID must be a positive number.");
        if (title == null || title.isBlank()) throw new IllegalArgumentException("Title required.");
        if (genre == null || genre.isBlank() || !genre.matches("[a-zA-Z ]+"))
            throw new IllegalArgumentException("Genre must only contain letters.");
        if (platform == null || platform.isBlank()) throw new IllegalArgumentException("Platform required.");

        this.id = id;
        this.title = title.trim();
        this.genre = genre.trim();
        this.platform = platform.trim();
        this.completed = completed;
    }

    /**
     * Gets the game ID, Title, Genre, Platform, and check if the game is completed.
     *
     * @return the game ID, Title, Genre, Platform, and return true if completed, false otherwise.
     */
    public long getId() { return id; }
    public String getTitle() { return title; }
    public String getGenre() { return genre; }
    public String getPlatform() { return platform; }
    public boolean isCompleted() { return completed; }

    /**
     * Updates the title of the game if valid.
     *
     * @param title New title (cannot be null or blank)
     */
    public void setTitle(String title) {
        if (title != null && !title.isBlank()) this.title = title.trim();
    }

    /**
     * Updates the genre of the game if valid.
     * Only allows letters and spaces.
     *
     * @param genre New genre (letters only)
     */
    public void setGenre(String genre) {
        if (genre != null && !genre.isBlank() && genre.matches("[a-zA-Z ]+"))
            this.genre = genre.trim();
        else
            System.out.println("Invalid genre. Only letters allowed.");
    }

    /**
     * Updates the platform of the game if valid.
     *
     * @param platform New platform (cannot be null or blank)
     */
    public void setPlatform(String platform) {
        if (platform != null && !platform.isBlank()) this.platform = platform.trim();
    }

    /**
     * Updates the completion status of the game.
     *
     * @param completed true if completed, false otherwise
     */
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    /**
     * Returns a formatted string containing all game details.
     *
     * @return formatted string representation of the game
     */
    @Override
    public String toString() {
        return "ID: " + id +
                ", Title: " + (title != null ? title : "N/A") +
                ", Genre: " + (genre != null ? genre : "N/A") +
                ", Platform: " + (platform != null ? platform : "N/A") +
                ", Completed: " + completed;
    }
}