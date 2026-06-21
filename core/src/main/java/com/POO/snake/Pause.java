package com.POO.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.ScreenUtils;

/**
 * The pause overlay screen displayed when gameplay is temporarily suspended.
 * Preserves the previous game state and allows players to resume or exit the application.
 *
 * @author Davi N. P.
 * @author Daniel A. M.
 * @author Gustavo S. L.
 * @version 1.0
 */
public class Pause extends ScreenAdapter {

    private SnakeGame game;
    private GameScreen gameScreen; 
    private BitmapFont font;
    private GlyphLayout layout;
    private KeyboardController controller;

    /**
     * Initializes the Pause screen overlay.
     *
     * @param game       The main game instance.
     * @param gameScreen The active gameplay instance that was put on hold.
     */
    public Pause(SnakeGame game, GameScreen gameScreen) {
        this.game = game;
        this.gameScreen = gameScreen;
        this.font = new BitmapFont();
        this.layout = new GlyphLayout();

        this.controller = new KeyboardController(this);
        Gdx.input.setInputProcessor(controller);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.1f, 0.1f, 0.15f, 1);

        float screenWidth = Gdx.graphics.getWidth();
        game.getBatch().begin();

        font.setColor(Color.ORANGE);
        font.getData().setScale(3f);
        layout.setText(font, "PAUSE");
        font.draw(game.getBatch(), "PAUSE", (screenWidth - layout.width) / 2, 400);

        font.setColor(Color.WHITE);
        font.getData().setScale(1.2f);
        layout.setText(font, "Press ENTER to Continue");
        font.draw(game.getBatch(), "Press ENTER to Continue", (screenWidth - layout.width) / 2, 250);

        font.setColor(Color.LIGHT_GRAY);
        layout.setText(font, "Press ESC to Close");
        font.draw(game.getBatch(), "Press ESC to Close", (screenWidth - layout.width) / 2, 200);

        game.getBatch().end();
    }

    /**
     * Restores input mapping and renders the previously paused game instance.
     */
    public void resumeGame() {
        Gdx.input.setInputProcessor(gameScreen.getController());
        game.setScreen(gameScreen);
    }

    @Override
    public void dispose() {
        font.dispose();
    }
}