package com.POO.snake;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.RepeatedTest;

/**
 * Unit tests for the {@link Apple} class.
 * Uses {@link RepeatedTest} because the apple uses {@link java.util.Random};
 * repeating the test 50 times ensures the random generation respects all
 * invariants across multiple seeds.
 */
public class AppleTest {

    @RepeatedTest(50)
    public void testAppleCoordinatesBoundsAndMultiples() {
        Apple apple = new Apple();
        int x = apple.getX();
        int y = apple.getY();

        int maxX = (GameConfig.GRID_COLS - 1) * GameConfig.TILE_SIZE;
        int maxY = (GameConfig.GRID_ROWS - 1) * GameConfig.TILE_SIZE;

        assertTrue(x >= 0 && x <= maxX,
                "X must be within grid bounds [0, " + maxX + "]. Got: " + x);
        assertEquals(0, x % GameConfig.TILE_SIZE,
                "X must be a multiple of TILE_SIZE (" + GameConfig.TILE_SIZE + "). Got: " + x);

        assertTrue(y >= 0 && y <= maxY,
                "Y must be within grid bounds [0, " + maxY + "]. Got: " + y);
        assertEquals(0, y % GameConfig.TILE_SIZE,
                "Y must be a multiple of TILE_SIZE (" + GameConfig.TILE_SIZE + "). Got: " + y);
    }
}
