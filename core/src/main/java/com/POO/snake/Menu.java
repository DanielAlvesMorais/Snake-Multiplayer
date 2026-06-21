package com.POO.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.ScreenUtils;

/**
 * The main menu screen presented when the game is launched.
 * Provides navigation to start a new game or view the local ranking.
 *
 * @author Davi N. P.
 * @author Daniel A. M.
 * @author Gustavo S. L.
 * @version 1.0
 */
public class Menu extends ScreenAdapter {

    private SnakeGame game;
    private BitmapFont font;
    private KeyboardController controller;
    private GlyphLayout layout;

    /**
     * Initializes the Menu interface and assigns the keyboard input processor.
     *
     * @param game The main game instance.
     */
    public Menu(SnakeGame game) {
        this.game = game;

        this.controller = new KeyboardController(this);
        this.font = new BitmapFont();
        this.layout = new GlyphLayout();
        Gdx.input.setInputProcessor(controller);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.NAVY);

        float screenWidth = Gdx.graphics.getWidth();

        game.getBatch().begin();

        font.setColor(Color.WHITE);
        font.getData().setScale(2f);
        layout.setText(font, "SNAKE MULTIPLAYER");

        float titleX = (screenWidth - layout.width) / 2;
        font.draw(game.getBatch(), "SNAKE MULTIPLAYER", titleX, 350);

        font.setColor(Color.LIGHT_GRAY);
        font.getData().setScale(1.2f);
        layout.setText(font, "Pressione Enter para Jogar");

        float subtitleX = (screenWidth - layout.width) / 2;
        font.draw(game.getBatch(), "Pressione Enter para Jogar", subtitleX, 250);

        font.setColor(Color.LIGHT_GRAY);
        font.getData().setScale(1.2f);
        layout.setText(font, "Pressione P para pausar"); 

        float pauseX = (screenWidth - layout.width) / 2;
        font.draw(game.getBatch(), "Pressione P para pausar", pauseX, 150);

        font.setColor(Color.LIGHT_GRAY);
        font.getData().setScale(1.2f);
        layout.setText(font, "Pressione R para acessar o ranking");

        float rankX = (screenWidth - layout.width) / 2;
        font.draw(game.getBatch(), "Pressione R para acessar o ranking", rankX, 180);

        game.getBatch().end();
    }

    /**
     * Dispatches a screen change to start a new GameScreen session.
     */
    public void enterGame() {
        game.setScreen(new GameScreen(game));
    }

    /**
     * Dispatches a screen change to open the RankScreen.
     */
    public void enterRanking() {
        game.setScreen(new RankScreen(game));
    }

    @Override
    public void dispose() {
        font.dispose();
    }

    /**
     * Refreshes the menu state by instantiating a clean copy of the Menu screen.
     */
    public void returnToMenu() {
        game.setScreen(new Menu(game));
    }
}