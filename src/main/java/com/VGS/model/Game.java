package com.VGS.model;

public class Game {
    private long id;
    private String title;
    private String genre;
    private String platform;
    private boolean completed;

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

    // Setters with validation
    public void setTitle(String title) {
        if (title != null && !title.isBlank()) this.title = title.trim();
    }

    public void setGenre(String genre) {
        if (genre != null && !genre.isBlank() && genre.matches("[a-zA-Z ]+"))
            this.genre = genre.trim();
        else
            System.out.println("Invalid genre. Only letters allowed.");
    }

    public void setPlatform(String platform) {
        if (platform != null && !platform.isBlank()) this.platform = platform.trim();
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @Override
    public String toString() {
        return "ID: " + id +
                ", Title: " + (title != null ? title : "N/A") +
                ", Genre: " + (genre != null ? genre : "N/A") +
                ", Platform: " + (platform != null ? platform : "N/A") +
                ", Completed: " + completed;
    }
}