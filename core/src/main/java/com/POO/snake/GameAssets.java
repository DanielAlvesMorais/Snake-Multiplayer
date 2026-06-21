package com.POO.snake;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 * Centralized repository for all textures and fonts used in the game.
 * Assets are loaded once upon creation and shared across different screens to optimize memory.
 *
 * @author Davi N. P.
 * @author Daniel A. M.
 * @author Gustavo S. L.
 * @version 1.0
 */
public class GameAssets {
    public final Texture appleTexture;
    public final Texture backgroundTexture;

    // Snake 1 textures
    public final Texture snake1HeadTexture;
    public final Texture snake1BodyTexture;
    public final Texture snake1TailTexture;
    public final Texture snake1CornerTexture;

    // Snake 2 textures
    public final Texture snake2HeadTexture;
    public final Texture snake2BodyTexture;
    public final Texture snake2TailTexture;
    public final Texture snake2CornerTexture;

    public final BitmapFont font;

    /**
     * Loads every required texture and font from the disk into memory.
     */
    public GameAssets() {
        appleTexture = new Texture("apple.png");
        backgroundTexture = new Texture("background.png");

        snake1HeadTexture = new Texture("snake1_head.png");
        snake1BodyTexture = new Texture("snake1_body.png");
        snake1TailTexture = new Texture("snake1_tail.png");
        snake1CornerTexture = new Texture("snake1_corner.png");

        snake2HeadTexture = new Texture("snake2_head.png");
        snake2BodyTexture = new Texture("snake2_body.png");
        snake2TailTexture = new Texture("snake2_tail.png");
        snake2CornerTexture = new Texture("snake2_corner.png");

        font = new BitmapFont();
        font.getData().setScale(1.2f);
    }

    /**
     * Safely releases all textures and fonts from the GPU/CPU memory.
     */
    public void dispose() {
        appleTexture.dispose();
        if (backgroundTexture != null) {
            backgroundTexture.dispose();
        }
        snake1HeadTexture.dispose();
        snake1BodyTexture.dispose();
        snake1TailTexture.dispose();
        snake1CornerTexture.dispose();

        snake2HeadTexture.dispose();
        snake2BodyTexture.dispose();
        snake2TailTexture.dispose();
        snake2CornerTexture.dispose();

        font.dispose();
    }
}