package com.POO.snake;

/**
 * A basic data structure representing a single ranking entry,
 * associating a player's initial/name with their achieved score.
 * Designed to be serialized to and from JSON files.
 *
 * @author Davi N. P.
 * @author Daniel A. M.
 * @author Gustavo S. L.
 * @version 1.0
 */
public class PlayerScore {

    private String name;
    private int points;

    /**
     * Default no-argument constructor required for JSON serialization.
     */
    public PlayerScore() {
    }

    /**
     * Constructs a populated PlayerScore instance.
     *
     * @param name   The string initials identifier for the player.
     * @param points The integer total score achieved.
     */
    public PlayerScore(String name, int points) {
        this.name = name;
        this.points = points;
    }

    /**
     * Retrieves the player's name identifier.
     * @return The player name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the player's name identifier.
     * @param name The new player name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieves the player's total points.
     * @return The score points.
     */
    public int getPoints() {
        return points;
    }

    /**
     * Sets the player's total points.
     * @param points The new score points.
     */
    public void setPoints(int points) {
        this.points = points;
    }
}