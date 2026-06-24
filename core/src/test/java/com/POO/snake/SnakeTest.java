package com.POO.snake;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link Snake} class.
 * <p>
 * Because {@link Snake} no longer depends on LibGDX audio, no OpenGL mock setup
 * is required — these tests run as plain JUnit 5 without any framework bootstrap.
 * </p>
 */
public class SnakeTest {

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
        snake.move();
        SnakeBody newHead = snake.getBody().peekFirst();
        assertEquals(100, newHead.getX());
        assertEquals(120, newHead.getY());  // moved UP by TILE_SIZE (20)
    }

    @Test
    public void testValidAndInvalidDirectionChange() {
        Snake snake = new Snake(100, 100);

        // Opposite direction (UP → DOWN) must be rejected.
        snake.setDirection(Direction.DOWN);
        assertEquals(Direction.UP, snake.getDirection(),
                "Should not allow reversing direction");

        // Valid turn (UP → RIGHT) must be accepted.
        snake.setDirection(Direction.RIGHT);
        assertEquals(Direction.RIGHT, snake.getDirection(),
                "Should allow turning right");
    }

    @Test
    public void testEatingAppleIncreasesScoreAndSize() {
        Snake snake = new Snake(100, 100);
        int initialSize = snake.getBody().size();

        assertTrue(snake.eatApple());
        assertEquals(1, snake.getScore(), "Score must increase to 1 after eating");

        snake.move();
        assertEquals(initialSize + 1, snake.getBody().size(),
                "Body size must grow by 1 after eating and moving");
    }

    @Test
    public void testSelfCollisionDetected() {
        Snake snake = new Snake(100, 100);
        // Manually add a duplicate segment at the head position to simulate wrapping.
        snake.getBody().add(new SnakeBody(100, 100));

        Snake dummy = new Snake(500, 500);
        assertTrue(snake.checkCollision(dummy),
                "Self-collision must be detected");
    }

    @Test
    public void testNoFalseCollisionWhenFar() {
        Snake snake1 = new Snake(100, 100);
        Snake snake2 = new Snake(400, 400);
        assertFalse(snake1.checkCollision(snake2),
                "Should report no collision when snakes are far apart");
    }

    @Test
    public void testCrossCollisionWithOpponent() {
        Snake snake1 = new Snake(100, 100);
        Snake snake2 = new Snake(100, 100); // Spawned on top of snake1
        assertTrue(snake1.checkCollision(snake2),
                "Cross-collision must be detected when heads overlap");
    }

    @Test
    public void testMultipleDirectionChangesInOneTick() {
        Snake snake = new Snake(100, 100);
        // First change in this tick should succeed.
        snake.setDirection(Direction.RIGHT);
        assertEquals(Direction.RIGHT, snake.getDirection());

        // Second change before move() should be ignored (hasChangedDirection guard).
        snake.setDirection(Direction.DOWN);
        assertEquals(Direction.RIGHT, snake.getDirection(),
                "Only one direction change per tick should be accepted");
    }

    @Test
    public void testWrappingTopBoundary() {
        // Place head near the top edge and move UP.
        int startY = GameConfig.PLAY_HEIGHT - GameConfig.TILE_SIZE; // last valid row
        Snake snake = new Snake(100, startY);
        snake.move();
        SnakeBody head = snake.getBody().peekFirst();
        assertEquals(0, head.getY(),
                "Moving past the top boundary should wrap to Y=0");
    }

    @Test
    public void testWrappingRightBoundary() {
        int startX = GameConfig.SCREEN_WIDTH - GameConfig.TILE_SIZE;
        Snake snake = new Snake(startX, 100);
        // Change to RIGHT first (snake starts facing UP).
        snake.setDirection(Direction.RIGHT);
        snake.move();
        SnakeBody head = snake.getBody().peekFirst();
        assertEquals(0, head.getX(),
                "Moving past the right boundary should wrap to X=0");
    }
}
