package com.POO.snake;

import java.util.Deque;
import java.util.LinkedList;

/**
 * Represents the snake entity within the game.
 * Uses a double-ended queue (Deque) to track the coordinates of each body segment,
 * handling grid-based movement, direction changes, growth, and collision detection.
 *
 * @author Davi N. P.
 * @author Daniel A. M.
 * @author Gustavo S. L.
 * @version 1.0
 */
public class Snake {

    private Deque<SnakeBody> body = new LinkedList<>();
    private Direction direction;
    private boolean hasChangedDirection = false;
    private boolean hasEatenApple = false;
    private int score = 0;

    /**
     * Constructs a new snake with an initial size of 3 segments, facing UP.
     *
     * @param x The starting X coordinate for the head.
     * @param y The starting Y coordinate for the head.
     */
    public Snake(int x, int y) {
        body.addFirst(new SnakeBody(x, y));
        body.add(new SnakeBody(x, y - 20));
        body.add(new SnakeBody(x, y - 40));
        this.direction = Direction.UP;
    }

    /**
     * Attempts to update the snake's travel direction.
     * Prevents multiple direction changes in a single tick and ignores attempts to reverse
     * directly into its own body.
     *
     * @param direction The new intended Direction.
     */
    public void setDirection(Direction direction) {
        if (this.hasChangedDirection) {
            return;
        }
        if ((this.direction == Direction.UP && direction == Direction.DOWN)
                || (this.direction == Direction.DOWN && direction == Direction.UP)
                || (this.direction == Direction.LEFT && direction == Direction.RIGHT)
                || (this.direction == Direction.RIGHT && direction == Direction.LEFT)) {
            return;
        }
        
        this.direction = direction;
        this.hasChangedDirection = true;
        SoundManager.getInstance().playMove();
    }

    /**
     * Calculates the next position of the head based on the current direction,
     * wraps the position around screen boundaries, and updates the body queue.
     */
    public void move() {
        SnakeBody head = body.peekFirst();
        int newX = head.getX();
        int newY = head.getY();

        switch (this.direction) {
            case UP:
                newY += 20;
                if (newY >= 440) {  
                    newY = 0;
                }
                break;
            case DOWN:
                newY -= 20;
                if (newY < 0) {
                    newY = 440 - 20; 
                }
                break;
            case LEFT:
                newX -= 20;
                if (newX < 0) {
                    newX = 620;
                }
                break;
            case RIGHT:
                newX += 20;
                if (newX >= 640) {
                    newX = 0;
                }
                break;
        }

        SnakeBody newHead = new SnakeBody(newX, newY);
        body.addFirst(newHead);
        
        if (this.hasEatenApple) {
            this.hasEatenApple = false;
        } else {
            body.removeLast();
        }
        
        this.hasChangedDirection = false;
    }

    /**
     * Flags the snake to grow on its next movement and increments its score.
     *
     * @return True to confirm the apple was eaten.
     */
    public boolean eatApple() {
        this.hasEatenApple = true;
        this.score++;
        return this.hasEatenApple;
    }

    /**
     * Retrieves the current score accumulated by this snake.
     *
     * @return The integer score.
     */
    public int getScore() {
        return this.score;
    }

    /**
     * Checks if this snake's head has collided with its own body or with the opposing snake.
     *
     * @param otherSnake The opponent snake to check against.
     * @return True if a collision occurred, false otherwise.
     */
    public boolean checkCollision(Snake otherSnake) {
        SnakeBody head = body.peekFirst();
        
        for (SnakeBody part : body) {
            if (part != head && part.getX() == head.getX() && part.getY() == head.getY()) {
                return true;
            }
        }
        
        for (SnakeBody part : otherSnake.getBody()) {
            if (part.getX() == head.getX() && part.getY() == head.getY()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retrieves the double-ended queue representing the ordered segments of the snake.
     *
     * @return The body Deque.
     */
    public Deque<SnakeBody> getBody() {
        return this.body;
    }

    /**
     * Retrieves the current direction of movement.
     *
     * @return The Direction enum value.
     */
    public Direction getDirection() {
        return this.direction;
    }
}

/**
 * Enumeration representing the four possible orthogonal movement directions.
 */
enum Direction {
    UP, DOWN, LEFT, RIGHT
}