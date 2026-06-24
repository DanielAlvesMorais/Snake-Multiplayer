package com.POO.snake;

import java.util.Random;

/**
 * Represents the apple that the snakes consume to grow in size and increase their score.
 * Handles its own random grid-based positioning logic using the global
 * {@link GameConfig} constants to guarantee alignment and in-bounds placement.
 *
 * @author Daniel A. M.
 * @author Davi N. P.
 * @author Gustavo S. L.
 * @version 1.0
 */
public class Apple {

    private int x;
    private int y;

    /**
     * Instantiates an Apple and assigns it a random coordinate within the game
     * grid boundaries, aligned to the tile grid.
     */
    public Apple() {
        Random random = new Random();
        this.x = random.nextInt(GameConfig.GRID_COLS) * GameConfig.TILE_SIZE;
        this.y = random.nextInt(GameConfig.GRID_ROWS) * GameConfig.TILE_SIZE;
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
