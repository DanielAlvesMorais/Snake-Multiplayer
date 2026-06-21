package com.POO.snake;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class PlayerScoreTest {

    @Test
    public void testFullConstructorAndGetters() {
        PlayerScore player = new PlayerScore("ABC", 150);
        
        assertEquals("ABC", player.getName(), "The player's name should be ABC");
        assertEquals(150, player.getPoints(), "The player's score should be 150");
    }

    @Test
    public void testEmptyConstructorAndSetters() {
        PlayerScore player = new PlayerScore();
        player.setName("XYZ");
        player.setPoints(42);
        
        assertEquals("XYZ", player.getName(), "The setter failed to define the name");
        assertEquals(42, player.getPoints(), "The setter failed to define the score");
    }
}