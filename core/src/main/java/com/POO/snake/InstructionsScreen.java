package com.POO.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.ScreenUtils;

/**
 * Screen that displays the game controls, scoring rules, and board behaviour
 * to the players before they start a match.
 * <p>
 * This screen fulfils the "instructions" requirement listed in the project
 * specification and is accessible from the {@link Menu} via the {@code I} key.
 * </p>
 *
 * @author Daniel A. M.
 * @author Davi N. P.
 * @author Gustavo S. L.
 * @version 1.0
 */
public class InstructionsScreen extends ScreenAdapter {

    private final SnakeGame game;
    private final BitmapFont font;
    private final GlyphLayout layout;

    /**
     * Initializes the Instructions screen and configures the input processor to
     * return to the main menu when ESC is pressed.
     *
     * @param game The main game instance.
     */
    public InstructionsScreen(SnakeGame game) {
        this.game   = game;
        this.font   = new BitmapFont();
        this.layout = new GlyphLayout();

        Gdx.input.setInputProcessor(new com.badlogic.gdx.InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Keys.ESCAPE || keycode == Keys.BACKSPACE) {
                    game.setScreen(new Menu(game));
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0f, 0f, 0.15f, 1f);

        float screenWidth  = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        game.getBatch().begin();

        // --- Title ---
        font.getData().setScale(2f);
        font.setColor(Color.YELLOW);
        String title = "HOW TO PLAY";
        layout.setText(font, title);
        font.draw(game.getBatch(), title, (screenWidth - layout.width) / 2f, screenHeight - 30);

        // --- Section: Controls ---
        font.getData().setScale(1.4f);
        font.setColor(Color.WHITE);
        drawLeft("CONTROLS", screenWidth, 360);

        font.getData().setScale(1.1f);
        font.setColor(Color.LIGHT_GRAY);
        drawLeft("Player 1 (Green):  Arrow Keys",     screenWidth, 330);
        drawLeft("Player 2 (Blue):   W / A / S / D",  screenWidth, 305);
        drawLeft("Pause / Resume:    ESC",             screenWidth, 280);

        // --- Section: Gameplay ---
        font.getData().setScale(1.4f);
        font.setColor(Color.WHITE);
        drawLeft("GAMEPLAY", screenWidth, 240);

        font.getData().setScale(1.1f);
        font.setColor(Color.LIGHT_GRAY);
        drawLeft("Eat apples to grow and earn points.", screenWidth, 210);
        drawLeft("Each apple eaten also increases your speed.", screenWidth, 185);
        drawLeft("The board wraps around: exiting one edge", screenWidth, 160);
        drawLeft("teleports your snake to the opposite side.", screenWidth, 135);

        // --- Section: Losing ---
        font.getData().setScale(1.4f);
        font.setColor(Color.WHITE);
        drawLeft("GAME OVER", screenWidth, 100);

        font.getData().setScale(1.1f);
        font.setColor(Color.LIGHT_GRAY);
        drawLeft("Colliding with your own body or the opponent ends the match.", screenWidth, 70);

        // --- Back prompt ---
        font.getData().setScale(1f);
        font.setColor(Color.GRAY);
        String prompt = "Press ESC to return to Menu";
        layout.setText(font, prompt);
        font.draw(game.getBatch(), prompt, (screenWidth - layout.width) / 2f, 30);

        game.getBatch().end();
    }

    /**
     * Draws a left-aligned (padded by 60 px) string at the given Y coordinate.
     *
     * @param text        The string to render.
     * @param screenWidth Unused for left-alignment but kept for signature consistency.
     * @param y           The vertical position.
     */
    private void drawLeft(String text, float screenWidth, float y) {
        font.draw(game.getBatch(), text, 60, y);
    }

    @Override
    public void dispose() {
        font.dispose();
    }
}
