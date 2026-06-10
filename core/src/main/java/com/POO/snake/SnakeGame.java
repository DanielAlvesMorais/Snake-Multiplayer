package com.POO.snake;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class SnakeGame extends Game {
    private SpriteBatch batch;
    // private Texture image;

    @Override
    public void create() {
        batch = new SpriteBatch();

        this.setScreen(new Menu(this));
    }

    public SpriteBatch getBatch() {
        return this.batch;
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
