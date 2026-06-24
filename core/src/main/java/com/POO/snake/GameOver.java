package com.POO.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.ScreenUtils;

/**
 * Screen displayed after a match concludes.
 * Shows the winner, final score, and handles the logic for capturing player initials
 * if their score qualifies for the top ranking.
 * <p>
 * {@link GameAssets} is instantiated once in the constructor and disposed in
 * {@link #dispose()} — never inside the render loop — to avoid per-frame
 * texture reloads and the associated memory leak.
 * </p>
 *
 * @author Davi N. P.
 * @author Daniel A. M.
 * @author Gustavo S. L.
 * @version 1.0
 */
public class GameOver extends ScreenAdapter {

    private final SnakeGame game;
    private final BitmapFont font;
    private final GlyphLayout layout;
    private final KeyboardController controller;
    private final GameAssets assets;

    private final String winner;
    private final int finalScore;

    private static final int SPRITE_SIZE        = 80;
    private static final int MINIMUM_RANK_SCORE = 1;

    private final StringBuilder initials = new StringBuilder();
    private boolean waitingInitials;

    /**
     * Initializes the Game Over screen with match statistics.
     * Assets are loaded once here so they can be safely reused across every
     * render call and properly released in {@link #dispose()}.
     *
     * @param game       The main game instance.
     * @param winner     A string identifier for the winner
     *                   ({@code "P1"}, {@code "P2"}, or {@code "Tie"}).
     * @param finalScore The highest score achieved in the match.
     */
    public GameOver(SnakeGame game, String winner, int finalScore) {
        this.game       = game;
        this.winner     = winner;
        this.finalScore = finalScore;
        this.font       = new BitmapFont();
        this.layout     = new GlyphLayout();
        // Load assets ONCE here, not inside render().
        this.assets     = new GameAssets();

        this.waitingInitials = !winner.equals("Tie") && finalScore >= MINIMUM_RANK_SCORE;

        this.controller = new KeyboardController(this);
        Gdx.input.setInputProcessor(controller);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0f, 0.2f, 0f, 1);
        float screenWidth = Gdx.graphics.getWidth();

        // assets is a field — no allocation happens here.
        game.getBatch().begin();

        font.getData().setScale(2.5f);
        if (winner.equals("Tie")) {
            font.setColor(Color.YELLOW);
            drawCenteredText("TIE!", 400, screenWidth);
            game.getBatch().draw(assets.snake1HeadTexture,
                    (screenWidth / 2) - 100, 280, SPRITE_SIZE, SPRITE_SIZE);
            game.getBatch().draw(assets.snake2HeadTexture,
                    (screenWidth / 2) + 20,  280, SPRITE_SIZE, SPRITE_SIZE);
        } else {
            font.setColor(Color.GREEN);
            drawCenteredText(winner + " WINS!", 400, screenWidth);
            Texture winnerHead = winner.equals("P1")
                    ? assets.snake1HeadTexture
                    : assets.snake2HeadTexture;
            game.getBatch().draw(winnerHead,
                    (screenWidth - SPRITE_SIZE) / 2f, 280, SPRITE_SIZE, SPRITE_SIZE);
        }

        font.getData().setScale(1.5f);
        font.setColor(Color.WHITE);
        drawCenteredText("Score: " + finalScore + " pts", 240, screenWidth);

        if (waitingInitials) {
            font.getData().setScale(1.2f);
            font.setColor(Color.CYAN);
            drawCenteredText("You made the Ranking! Enter 3 initials:", 180, screenWidth);

            String initialsText = initials.toString();
            while (initialsText.length() < 3) {
                initialsText += " _";
            }
            font.getData().setScale(2.5f);
            font.setColor(Color.WHITE);
            drawCenteredText(initialsText.toUpperCase(), 130, screenWidth);
        } else {
            font.getData().setScale(1.1f);
            font.setColor(Color.WHITE);
            drawCenteredText("Press ENTER to Play Again",   160, screenWidth);
            font.setColor(Color.LIGHT_GRAY);
            drawCenteredText("Press R to view Ranking",     120, screenWidth);
            drawCenteredText("Press ESC to close the game",  80, screenWidth);
        }

        game.getBatch().end();
    }

    /**
     * Draws a string horizontally centred on the screen at the given Y position.
     *
     * @param text        The string to render.
     * @param y           Vertical position in screen coordinates.
     * @param screenWidth Total screen width used for centring calculation.
     */
    private void drawCenteredText(String text, float y, float screenWidth) {
        layout.setText(font, text);
        font.draw(game.getBatch(), text, (screenWidth - layout.width) / 2, y);
    }

    /**
     * Appends a character to the player's initials if the maximum length of 3
     * has not yet been reached.
     *
     * @param letter The character inputted by the user.
     */
    public void addLetter(char letter) {
        if (waitingInitials && initials.length() < 3) {
            initials.append(Character.toUpperCase(letter));
        }
    }

    /**
     * Deletes the last entered character from the player's initials.
     */
    public void removeLastLetter() {
        if (waitingInitials && initials.length() > 0) {
            initials.deleteCharAt(initials.length() - 1);
        }
    }

    /**
     * Confirms the entered initials and persists them to the ranking, or restarts
     * the game if the ranking phase is already completed.
     */
    public void confirmOrRestart() {
        if (waitingInitials) {
            if (initials.length() == 3) {
                game.getRanking().checkAndAddNewScore(
                        initials.toString().toUpperCase(), finalScore);
                waitingInitials = false;
            }
        } else {
            restartGame();
        }
    }

    /**
     * Checks if the screen is currently waiting for the player to enter initials.
     *
     * @return True if waiting for initials, false otherwise.
     */
    public boolean isWaitingForInitials() {
        return waitingInitials;
    }

    /** Transitions to a fresh {@link GameScreen}. */
    public void restartGame() {
        game.setScreen(new GameScreen(game));
    }

    /** Transitions to the {@link RankScreen}. */
    public void viewRanking() {
        game.setScreen(new RankScreen(game));
    }

    /** Transitions back to the main {@link Menu}. */
    public void returnToMenu() {
        game.setScreen(new Menu(game));
    }

    /**
     * Releases all resources owned by this screen.
     * Disposes the {@link GameAssets} instance loaded in the constructor,
     * preventing GPU/CPU memory leaks.
     */
    @Override
    public void dispose() {
        font.dispose();
        assets.dispose();
    }
}