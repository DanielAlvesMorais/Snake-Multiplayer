package com.POO.snake;

/**
 * A basic data structure representing a single spatial cell occupied by the snake.
 * Forms the building blocks used in the snake's Deque structure.
 *
 * @author Davi N. P.
 * @author Daniel A. M.
 * @author Gustavo S. L.
 * @version 1.0
 */
public class SnakeBody {
    private int x;
    private int y;

    /**
     * Constructs a body segment at the specified grid coordinates.
     *
     * @param x The X coordinate.
     * @param y The Y coordinate.
     */
    public SnakeBody(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Retrieves the X coordinate.
     *
     * @return The current X position.
     */
    public int getX() {
        return x;
    }

    /**
     * Retrieves the Y coordinate.
     *
     * @return The current Y position.
     */
    public int getY() {
        return y;
    }
}