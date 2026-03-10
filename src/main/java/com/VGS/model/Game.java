package com.VGS.model;

public class Game {
    private long id;
    private String title;
    private String genre;
    private String platform;
    private boolean completed;

    /* Constructor: Game
     *
     * Creates a new Game object with the given attributes.
     * Validates input to ensure ID is positive, title and platform
     * are not blank, and genre contains only letters.
     *
     *  - long id: Unique identifier for the game (must be positive)
     *  - String title: Name of the game (cannot be null or blank)
     *  - String genre: Game genre (letters only)
     *  - String platform: Platform for the game (cannot be null or blank)
     *  - boolean completed: Whether the game has been completed
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

    // Getters
    public long getId() { return id; }
    public String getTitle() { return title; }
    public String getGenre() { return genre; }
    public String getPlatform() { return platform; }
    public boolean isCompleted() { return completed; }

    /* Method: setTitle
     *
     * Updates the title of the game if the new title is valid.
     *  - String title: New title for the game (cannot be null or blank)
     */
    public void setTitle(String title) {
        if (title != null && !title.isBlank()) this.title = title.trim();
    }

    /* Method: setGenre
     *
     * Updates the genre of the game if the new genre is valid.
     * Prints an error message if the genre contains invalid characters.
     *  - String genre: New genre (letters only)
     */
    public void setGenre(String genre) {
        if (genre != null && !genre.isBlank() && genre.matches("[a-zA-Z ]+"))
            this.genre = genre.trim();
        else
            System.out.println("Invalid genre. Only letters allowed.");
    }

    /* Method: setPlatform
     *
     * Updates the platform of the game if the new value is valid.
     *  - String platform: New platform (cannot be null or blank)
     */
    public void setPlatform(String platform) {
        if (platform != null && !platform.isBlank()) this.platform = platform.trim();
    }

    /* Method: setCompleted
     *
     * Updates the completion status of the game.
     *  - boolean completed: True if the game is completed, false otherwise
     */
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    /* Method: toString
     *
     * Returns a formatted string containing all the game information.
     *
     * Arguments: none
     * Return: String - formatted game details
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