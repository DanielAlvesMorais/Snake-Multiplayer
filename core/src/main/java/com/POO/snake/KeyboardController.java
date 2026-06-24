package com.POO.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;

/**
 * Centralized input handler that processes keyboard events.
 * It maps user keystrokes to the appropriate game actions depending on the active
 * screen context.
 * <p>
 * Audio feedback for direction changes is triggered here rather than inside
 * {@link Snake#setDirection(Direction)}, keeping {@code Snake} free of audio
 * dependencies and preserving the MVC separation.
 * </p>
 *
 * @author Daniel A. M.
 * @author Davi N. P.
 * @author Gustavo S. L.
 * @version 1.0
 */
public class KeyboardController extends InputAdapter {

    private Snake snake1;
    private Snake snake2;
    private GameScreen gameScreen;

    private Menu menuScreen;
    private Pause pauseScreen;
    private GameOver gameOverScreen;
    private RankScreen rankScreen;

    /**
     * Constructor specifically for the in-game state, tracking player movements
     * and pause actions.
     *
     * @param gameScreen The active gameplay screen.
     * @param snake1     Reference to the first snake entity.
     * @param snake2     Reference to the second snake entity.
     */
    public KeyboardController(GameScreen gameScreen, Snake snake1, Snake snake2) {
        this.gameScreen = gameScreen;
        this.snake1 = snake1;
        this.snake2 = snake2;
    }

    /**
     * Constructor for routing Menu screen inputs.
     *
     * @param menuScreen The active menu screen.
     */
    public KeyboardController(Menu menuScreen) {
        this.menuScreen = menuScreen;
    }

    /**
     * Constructor for routing Pause screen inputs.
     *
     * @param pauseScreen The active pause overlay screen.
     */
    public KeyboardController(Pause pauseScreen) {
        this.pauseScreen = pauseScreen;
    }

    /**
     * Constructor for routing Game Over screen inputs (including initial inputs).
     *
     * @param gameOverScreen The active game over screen.
     */
    public KeyboardController(GameOver gameOverScreen) {
        this.gameOverScreen = gameOverScreen;
    }

    /**
     * Constructor for routing Ranking screen inputs.
     *
     * @param rankScreen The active ranking screen.
     */
    public KeyboardController(RankScreen rankScreen) {
        this.rankScreen = rankScreen;
    }

    @Override
    public boolean keyDown(int keycode) {

        // --- IN-GAME CONTROLS ---
        if (snake1 != null && snake2 != null && gameScreen != null) {
            switch (keycode) {
                case Keys.ESCAPE:
                    gameScreen.game.setScreen(new Pause(gameScreen.game, gameScreen));
                    return true;
                case Keys.UP:
                    changeDirection(snake1, Direction.UP);
                    return true;
                case Keys.DOWN:
                    changeDirection(snake1, Direction.DOWN);
                    return true;
                case Keys.LEFT:
                    changeDirection(snake1, Direction.LEFT);
                    return true;
                case Keys.RIGHT:
                    changeDirection(snake1, Direction.RIGHT);
                    return true;
                case Keys.W:
                    changeDirection(snake2, Direction.UP);
                    return true;
                case Keys.S:
                    changeDirection(snake2, Direction.DOWN);
                    return true;
                case Keys.A:
                    changeDirection(snake2, Direction.LEFT);
                    return true;
                case Keys.D:
                    changeDirection(snake2, Direction.RIGHT);
                    return true;
                default:
                    return false;
            }
        }

        // --- MENU CONTROLS ---
        if (menuScreen != null) {
            switch (keycode) {
                case Keys.ENTER:
                    menuScreen.enterGame();
                    return true;
                case Keys.R:
                    menuScreen.enterRanking();
                    return true;
                case Keys.I:
                    menuScreen.enterInstructions();
                    return true;
                default:
                    return false;
            }
        }

        // --- PAUSE CONTROLS ---
        if (pauseScreen != null) {
            switch (keycode) {
                case Keys.ENTER:
                    pauseScreen.resumeGame();
                    return true;
                case Keys.ESCAPE:
                    Gdx.app.exit();
                    return true;
                default:
                    return false;
            }
        }

        // --- RANKING CONTROLS ---
        if (rankScreen != null) {
            switch (keycode) {
                case Keys.ESCAPE:
                    rankScreen.backToMenu();
                    return true;
                default:
                    return false;
            }
        }

        // --- GAME OVER CONTROLS ---
        if (gameOverScreen != null) {
            if (gameOverScreen.isWaitingForInitials()) {
                switch (keycode) {
                    case Keys.ENTER:
                        gameOverScreen.confirmOrRestart();
                        return true;
                    case Keys.BACKSPACE:
                        gameOverScreen.removeLastLetter();
                        return true;
                    default:
                        return false;
                }
            } else {
                switch (keycode) {
                    case Keys.ENTER:
                        gameOverScreen.restartGame();
                        return true;
                    case Keys.R:
                        gameOverScreen.viewRanking();
                        return true;
                    case Keys.ESCAPE:
                        Gdx.app.exit();
                        return true;
                    default:
                        return false;
                }
            }
        }

        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        if (gameOverScreen != null && gameOverScreen.isWaitingForInitials()) {
            if (Character.isLetter(character)) {
                gameOverScreen.addLetter(character);
                return true;
            }
        }
        return false;
    }

    /**
     * Delegates a direction change to the given snake and plays the move sound effect.
     * Centralising audio here keeps {@link Snake} free of audio dependencies.
     *
     * @param snake     The snake whose direction should be updated.
     * @param direction The new desired direction.
     */
    private void changeDirection(Snake snake, Direction direction) {
        Direction before = snake.getDirection();
        snake.setDirection(direction);
        if (before != snake.getDirection()) {
            SoundManager.getInstance().playMove();
        }
    }
}
