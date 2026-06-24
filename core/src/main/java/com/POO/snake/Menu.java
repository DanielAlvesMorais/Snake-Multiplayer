package com.POO.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.ScreenUtils;

/**
 * The main menu screen presented when the game is launched.
 * Provides navigation to start a new game, view the local ranking, or read
 * the instructions.
 *
 * @author Daniel A. M.
 * @author Davi N. P.
 * @author Gustavo S. L.
 * @version 1.0
 */
public class Menu extends ScreenAdapter {

    private final SnakeGame game;
    private final BitmapFont font;
    private final KeyboardController controller;
    private final GlyphLayout layout;

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
        font.draw(game.getBatch(), "SNAKE MULTIPLAYER",
                (screenWidth - layout.width) / 2, 380);

        font.getData().setScale(1.2f);

        drawCenteredOption(Color.LIGHT_GRAY, "Press ENTER to Play",   300, screenWidth);
        drawCenteredOption(Color.LIGHT_GRAY, "Press I for Instructions", 250, screenWidth);
        drawCenteredOption(Color.LIGHT_GRAY, "Press R to view Ranking",  200, screenWidth);
        drawCenteredOption(Color.LIGHT_GRAY, "Press ESC to Pause (in-game)", 150, screenWidth);

        game.getBatch().end();
    }

    /**
     * Draws a single menu option string centred on the screen at the given Y position.
     *
     * @param color       Text colour.
     * @param text        The string to display.
     * @param y           Vertical position in screen coordinates.
     * @param screenWidth Total screen width used for centring.
     */
    private void drawCenteredOption(Color color, String text, float y, float screenWidth) {
        font.setColor(color);
        layout.setText(font, text);
        font.draw(game.getBatch(), text, (screenWidth - layout.width) / 2, y);
    }

    /**
     * Dispatches a screen change to start a new {@link GameScreen} session.
     */
    public void enterGame() {
        game.setScreen(new GameScreen(game));
    }

    /**
     * Dispatches a screen change to open the {@link RankScreen}.
     */
    public void enterRanking() {
        game.setScreen(new RankScreen(game));
    }

    /**
     * Dispatches a screen change to open the {@link InstructionsScreen}.
     */
    public void enterInstructions() {
        game.setScreen(new InstructionsScreen(game));
    }

    /**
     * Refreshes the menu state by instantiating a clean copy of the Menu screen.
     */
    public void returnToMenu() {
        game.setScreen(new Menu(game));
    }

    @Override
    public void dispose() {
        font.dispose();
    }
}
