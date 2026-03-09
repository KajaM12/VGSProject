package com.VGS.model;

public class Game {

    private int id;
    private String title;
    private String genre;
    private String platform;
    private boolean completed;

    public Game(int id, String title, String genre, String platform, boolean completed) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.platform = platform;
        this.completed = completed;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public String getPlatform() {
        return platform;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @Override
    public String toString() {
        return "ID: " + id +
                " | Title: " + title +
                " | Genre: " + genre +
                " | Platform: " + platform +
                " | Completed: " + completed;
    }
}