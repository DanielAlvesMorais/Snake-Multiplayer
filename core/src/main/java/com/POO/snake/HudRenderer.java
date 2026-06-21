package com.POO.snake;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

/**
 * Responsible for rendering the Heads-Up Display (HUD) during gameplay,
 * which includes player scores and the elapsed match time.
 *
 * @author Davi N. P.
 * @author Daniel A. M.
 * @author Gustavo S. L.
 * @version 1.0
 */
public class HudRenderer {

    private final SnakeGame game;
    private final ShapeRenderer shapeRenderer;
    private final GlyphLayout layout;

    /**
     * Initializes the HUD Renderer dependencies.
     *
     * @param game The main game instance.
     */
    public HudRenderer(SnakeGame game) {
        this.game = game;
        this.shapeRenderer = new ShapeRenderer();
        this.layout = new GlyphLayout();
    }

    /**
     * Draws the top graphical HUD bar and the updated text metrics onto the screen.
     *
     * @param assets   The loaded game assets (fonts).
     * @param score1   The current score for Player 1.
     * @param score2   The current score for Player 2.
     * @param gameTime The elapsed match time in seconds.
     */
    public void draw(GameAssets assets, int score1, int score2, float gameTime) {
        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(0, 440, 640, 40);
        shapeRenderer.end();

        int seconds = (int) gameTime % 60;
        int minutes = (int) gameTime / 60;

        game.getBatch().begin();
        assets.font.setColor(Color.WHITE);
        assets.font.draw(game.getBatch(), "P1: " + score1, 10, 472);
        assets.font.draw(game.getBatch(), "P2: " + score2, 530, 472);

        String time = String.format("%02d:%02d", minutes, seconds);
        layout.setText(assets.font, time);
        assets.font.draw(game.getBatch(), time, (640 - layout.width) / 2, 472);
        game.getBatch().end();
    }

    /**
     * Frees resources allocated by the ShapeRenderer.
     */
    public void dispose() {
        shapeRenderer.dispose();
    }
}