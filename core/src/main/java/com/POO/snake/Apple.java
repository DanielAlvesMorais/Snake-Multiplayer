package com.POO.snake;

import java.util.Random;

/**
 * Represents the apple that the snakes consume to grow in size and increase their score.
 * Handles its own random grid-based positioning logic.
 *
 * @author Davi N. P.
 * @author Daniel A. M.
 * @author Gustavo S. L.
 * @version 1.0
 */
public class Apple {

    private int x;
    private int y;

    /**
     * Instantiates an Apple and assigns it a random coordinate within the game grid boundaries.
     */
    public Apple() {
        Random random = new Random();
        this.x = random.nextInt(32) * 20;
        this.y = random.nextInt(22) * 20; // 22 cells × 20px = 440px max
    }

    /**
     * Retrieves the X coordinate of the apple.
     *
     * @return The current X position.
     */
    public int getX() {
        return x;
    }

    /**
     * Retrieves the Y coordinate of the apple.
     *
     * @return The current Y position.
     */
    public int getY() {
        return y;
    }
}