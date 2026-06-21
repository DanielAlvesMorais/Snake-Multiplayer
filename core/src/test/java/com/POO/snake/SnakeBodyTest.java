package com.POO.snake;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class SnakeBodyTest {

    @Test
    public void testCreationAndGetters() {
        SnakeBody bodyPart = new SnakeBody(100, 200);
        
        assertEquals(100, bodyPart.getX(), "The X coordinate must be 100");
        assertEquals(200, bodyPart.getY(), "The Y coordinate must be 200");
    }
}