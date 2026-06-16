package com.POO.snake;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class GameAssets {
    public final Texture appleTexture;
    public final Texture backgroundTexture;

    // Texturas da Cobra 1
    public final Texture snake1HeadTexture;
    public final Texture snake1BodyTexture;
    public final Texture snake1TailTexture;
    public final Texture snake1CornerTexture;

    // Texturas da Cobra 2
    public final Texture snake2HeadTexture;
    public final Texture snake2BodyTexture;
    public final Texture snake2TailTexture;
    public final Texture snake2CornerTexture;

    public final BitmapFont font;

    public GameAssets() {
        appleTexture = new Texture("New Piskel (21).png");
        backgroundTexture = new Texture("New Piskel (20).png");

        snake1HeadTexture = new Texture("snake2_head.png");
        snake1BodyTexture = new Texture("snake2_body.png");
        snake1TailTexture = new Texture("snake2_tail.png");
        snake1CornerTexture = new Texture("snake2_corner.png");

        snake2HeadTexture = new Texture("New Piskel (18).png");
        snake2BodyTexture = new Texture("New Piskel (14).png");
        snake2TailTexture = new Texture("New Piskel (15).png");
        snake2CornerTexture = new Texture("New Piskel (19).png");

        font = new BitmapFont();
        font.getData().setScale(1.2f);
    }

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