package com.POO.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.ScreenUtils;

/**
 * The main gameplay screen responsible for driving the game loop,
 * updating snake positions, handling collisions, and coordinating rendering.
 *
 * @author Daniel A. M.
 * @author Davi N. P.
 * @author Gustavo S. L.
 * @version 1.0
 */
public class GameScreen implements Screen {

    final SnakeGame game;

    private final Snake snake1;
    private final Snake snake2;
    private Apple apple;
    private final KeyboardController controller;

    private final GameAssets assets;
    private final SnakeRenderer snakeRenderer;
    private final HudRenderer hudRenderer;

    private float moveTimer_snake1 = 0;
    private float moveTimer_snake2 = 0;
    private float moveInterval_snake1 = 0.15f;
    private float moveInterval_snake2 = 0.15f;
    private static final float MIN_INTERVAL   = 0.025f;
    private static final float SPEED_INCREASE = 0.01f;

    private float gameTime = 0f;

    /**
     * Initializes a new match, creating the snakes, apple, renderers, and input controllers.
     *
     * @param game The main game instance.
     */
    public GameScreen(final SnakeGame game) {
        this.game = game;

        this.assets       = new GameAssets();
        this.snakeRenderer = new SnakeRenderer(game.getBatch());
        this.hudRenderer  = new HudRenderer(game);

        this.snake1 = new Snake(280, 240);
        this.snake2 = new Snake(360, 240);

        this.controller = new KeyboardController(this, snake1, snake2);

        this.apple = new Apple();
        repositionApple();

        Gdx.input.setInputProcessor(controller);
    }

    /**
     * Determines the winner of the match based on collision states and scores.
     *
     * @return A string representing the result ({@code "P1"}, {@code "P2"},
     *         or {@code "Tie"}).
     */
    private String determineWinner() {
        boolean snake1Collided = snake1.checkCollision(snake2);
        boolean snake2Collided = snake2.checkCollision(snake1);

        if (snake1Collided && snake2Collided) {
            if (snake1.getScore() > snake2.getScore()) return "P1";
            if (snake2.getScore() > snake1.getScore()) return "P2";
            return "Tie";
        }
        if (snake1Collided) return "P2";
        if (snake2Collided) return "P1";
        return "Tie";
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.0f, 0.5f, 0.0f, 1.0f);

        moveTimer_snake1 += delta;
        moveTimer_snake2 += delta;
        gameTime += delta;

        if (moveTimer_snake1 > moveInterval_snake1) {
            snake1.move();
            moveTimer_snake1 = 0;
        }
        if (moveTimer_snake2 > moveInterval_snake2) {
            snake2.move();
            moveTimer_snake2 = 0;
        }

        // Alpha: fraction of the current movement interval elapsed (for smooth rendering).
        float alpha1 = Math.min(moveTimer_snake1 / moveInterval_snake1, 1.0f);
        float alpha2 = Math.min(moveTimer_snake2 / moveInterval_snake2, 1.0f);

        if (checkAppleCollision(snake1)) {
            moveInterval_snake1 = Math.max(MIN_INTERVAL, moveInterval_snake1 - SPEED_INCREASE);
        }
        if (checkAppleCollision(snake2)) {
            moveInterval_snake2 = Math.max(MIN_INTERVAL, moveInterval_snake2 - SPEED_INCREASE);
        }

        if (snake1.checkCollision(snake2) || snake2.checkCollision(snake1)) {
            endMatch();
            return;
        }

        game.getBatch().begin();

        if (assets.backgroundTexture != null) {
            game.getBatch().draw(assets.backgroundTexture, 0, 0,
                    GameConfig.SCREEN_WIDTH, GameConfig.SCREEN_HEIGHT);
        }

        game.getBatch().draw(assets.appleTexture,
                apple.getX(), apple.getY(),
                GameConfig.TILE_SIZE, GameConfig.TILE_SIZE);

        snakeRenderer.draw(snake1,
                assets.snake1HeadTexture, assets.snake1BodyTexture,
                assets.snake1TailTexture, assets.snake1CornerTexture, alpha1);
        snakeRenderer.draw(snake2,
                assets.snake2HeadTexture, assets.snake2BodyTexture,
                assets.snake2TailTexture, assets.snake2CornerTexture, alpha2);

        game.getBatch().end();

        hudRenderer.draw(assets, snake1.getScore(), snake2.getScore(), gameTime);
    }

    /**
     * Checks whether the given snake's head intersects with the apple's coordinates.
     *
     * @param snake The snake to test for collision.
     * @return {@code true} if a collision occurred, {@code false} otherwise.
     */
    private boolean checkAppleCollision(Snake snake) {
        SnakeBody head = snake.getBody().peekFirst();
        if (apple.getX() == head.getX() && apple.getY() == head.getY()) {
            snake.eatApple();
            SoundManager.getInstance().playEat();
            repositionApple();
            return true;
        }
        return false;
    }

    /**
     * Generates a new random position for the apple, ensuring it does not spawn
     * inside any snake body.
     */
    private void repositionApple() {
        boolean validPosition = false;
        while (!validPosition) {
            apple = new Apple();
            validPosition = true;

            for (SnakeBody piece : snake1.getBody()) {
                if (apple.getX() == piece.getX() && apple.getY() == piece.getY()) {
                    validPosition = false;
                    break;
                }
            }
            if (!validPosition) continue;

            for (SnakeBody piece : snake2.getBody()) {
                if (apple.getX() == piece.getX() && apple.getY() == piece.getY()) {
                    validPosition = false;
                    break;
                }
            }
        }
    }

    /**
     * Triggers the end of the match: determines the winner, plays the collision sound,
     * disposes this screen's resources, and transitions to {@link GameOver}.
     */
    private void endMatch() {
        String winner = determineWinner();
        SoundManager.getInstance().playCollision();

        int score;
        switch (winner) {
            case "P1":  score = snake1.getScore(); break;
            case "P2":  score = snake2.getScore(); break;
            default:    score = Math.max(snake1.getScore(), snake2.getScore()); break;
        }

        game.setScreen(new GameOver(game, winner, score));
        // Explicitly dispose this screen's resources now that we've switched away.
        dispose();
    }

    @Override public void show()   {}
    @Override public void resize(int width, int height) {}
    @Override public void pause()  {}
    @Override public void resume() {}
    @Override public void hide()   {}

    /**
     * Retrieves the keyboard controller currently attached to this game screen.
     *
     * @return The active {@link KeyboardController}.
     */
    public KeyboardController getController() {
        return this.controller;
    }

    @Override
    public void dispose() {
        assets.dispose();
        hudRenderer.dispose();
    }
}