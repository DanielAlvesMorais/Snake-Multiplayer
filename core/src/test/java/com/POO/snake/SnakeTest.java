package com.POO.snake;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;

public class SnakeTest {

    // Sets up a mock LibGDX environment before running the tests
    // This prevents the SoundManager from breaking the test by not finding the sound card
    @BeforeAll
    public static void setUpLibGDX() {
        Gdx.audio = Mockito.mock(Audio.class);
        Gdx.files = Mockito.mock(Files.class);
        FileHandle mockFile = Mockito.mock(FileHandle.class);
        Sound mockSound = Mockito.mock(Sound.class);

        Mockito.when(Gdx.files.internal(Mockito.anyString())).thenReturn(mockFile);
        Mockito.when(Gdx.audio.newSound(mockFile)).thenReturn(mockSound);
    }

    @Test
    public void testSnakeCreation() {
        Snake snake = new Snake(300, 300);
        
        assertEquals(3, snake.getBody().size(), "The snake must start with a size of 3");
        assertEquals(Direction.UP, snake.getDirection(), "The initial direction must be UP");
        assertEquals(0, snake.getScore(), "The initial score must be 0");
    }

    @Test
    public void testBasicMovement() {
        Snake snake = new Snake(100, 100);
        // The initial head is at (100, 100) and the direction is UP
        snake.move();
        
        // Since it moved up, Y should increase by 20 (pixel size)
        SnakeBody newHead = snake.getBody().peekFirst();
        assertEquals(100, newHead.getX());
        assertEquals(120, newHead.getY());
    }

    @Test
    public void testValidAndInvalidDirectionChange() {
        Snake snake = new Snake(100, 100);
        
        // Attempts to change to the opposite direction (UP -> DOWN), which is forbidden
        snake.setDirection(Direction.DOWN);
        assertEquals(Direction.UP, snake.getDirection(), "It should not allow changing to the opposite direction");

        // Changes to a valid direction (UP -> RIGHT)
        snake.setDirection(Direction.RIGHT);
        assertEquals(Direction.RIGHT, snake.getDirection(), "It should allow changing to the right");
    }

    @Test
    public void testEatingAppleIncreasesScoreAndSize() {
        Snake snake = new Snake(100, 100);
        int initialSize = snake.getBody().size();
        
        // Simulates the snake eating the apple
        assertTrue(snake.eatApple());
        assertEquals(1, snake.getScore(), "The score must increase to 1");
        
        // Moves the snake. Since it ate the apple, it should not remove the tail this turn
        snake.move();
        assertEquals(initialSize + 1, snake.getBody().size(), "The snake's size must increase by 1");
    }

    @Test
    public void testCollisionWithItself() {
        Snake snake = new Snake(100, 100);
        // We force positions on the body to simulate it wrapping around itself
        snake.getBody().add(new SnakeBody(100, 100)); 
        
        assertTrue(snake.checkCollision(new Snake(500, 500)), "The snake must detect collision with itself");
    }
}