package com.POO.snake;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * The core application entry point inheriting from LibGDX's Game class.
 * Acts as the orchestrator for the entire application, holding globally shared
 * resources like the SpriteBatch and the persistent ranking system, and delegating
 * the render loop to the currently active screen.
 *
 * @author Davi N. P.
 * @author Daniel A. M.
 * @author Gustavo S. L.
 * @version 1.0
 */
public class SnakeGame extends Game {

    private SpriteBatch batch;
    private Rank ranking;

    /**
     * Called when the application is first created.
     * Initializes global resources and transitions to the main Menu.
     */
    @Override
    public void create() {
        batch = new SpriteBatch();
        ranking = new Rank();
        this.setScreen(new Menu(this));
    }

    /**
     * Provides access to the shared SpriteBatch to be used by various screens.
     * Centralizing the batch prevents heavy memory duplication.
     *
     * @return The active SpriteBatch.
     */
    public SpriteBatch getBatch() {
        return batch;
    }

    /**
     * Provides access to the game's ranking manager.
     *
     * @return The initialized Rank instance.
     */
    public Rank getRanking() {
        return ranking;
    }

    /**
     * Called when the application is destroyed.
     * Safely frees heavy graphical and audio resources from system memory.
     */
    @Override
    public void dispose() {
        if (batch != null) {
            batch.dispose();
        }
        if (SoundManager.getInstance() != null) {
            SoundManager.getInstance().dispose();
        }
    }
}