package com.POO.snake;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.RepeatedTest;

public class AppleTest {

    // We use RepeatedTest because the apple uses Random. 
    // Repeating ensures that the randomness respects the rules across multiple attempts.
    @RepeatedTest(50)
    public void testAppleCoordinatesBoundsAndMultiples() {
        Apple apple = new Apple();
        int x = apple.getX();
        int y = apple.getY();

        // Checks if X is within the bounds of the 32-square grid (0 to 620)
        assertTrue(x >= 0 && x <= 620, "The X coordinate must be between 0 and 620. Current value: " + x);
        // Checks if X is a multiple of 20 (sprite size)
        assertEquals(0, x % 20, "The X coordinate must be a multiple of 20. Current value: " + x);

        // Checks if Y is within the bounds of the 22-square grid (0 to 420)
        assertTrue(y >= 0 && y <= 420, "The Y coordinate must be between 0 and 420. Current value: " + y);
        // Checks if Y is a multiple of 20
        assertEquals(0, y % 20, "The Y coordinate must be a multiple of 20. Current value: " + y);
    }
}