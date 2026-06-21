package com.POO.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

/**
 * Singleton class that manages the loading and playback of all game sound effects.
 * Ensures only one instance of the sounds is kept in memory to optimize resource usage.
 *
 * @author Davi N. P.
 * @author Daniel A. M.
 * @author Gustavo S. L.
 * @version 1.0
 */
public class SoundManager {

    private static SoundManager instance;

    private Sound soundEat;
    private Sound soundCollision;
    private Sound soundMove;

    /**
     * Private constructor to prevent direct instantiation.
     * Loads the sound files once when the singleton is first created.
     */
    private SoundManager(){
        this.soundEat = Gdx.audio.newSound(Gdx.files.internal("570636__bsp7176__food.wav"));
        this.soundMove = Gdx.audio.newSound(Gdx.files.internal("570635__bsp7176__move.wav"));
        this.soundCollision = Gdx.audio.newSound(Gdx.files.internal("explosion.wav"));
    }

    /**
     * Retrieves the single shared instance of the SoundManager, creating it on first use.
     *
     * @return The active SoundManager instance.
     */
    public static SoundManager getInstance(){
        if(instance == null){
            instance = new SoundManager();
        }
        return instance;
    }

    /**
     * Plays the sound effect for when a snake eats an apple.
     */
    public void playEat(){
        soundEat.play();
    }

    /**
     * Plays the sound effect for a snake moving.
     */
    public void playMove(){
        soundMove.play();
    }

    /**
     * Plays the sound effect for when a collision occurs (Game Over).
     */
    public void playCollision(){
        soundCollision.play();
    }

    /**
     * Releases all loaded sounds from memory to prevent leaks when the game closes.
     */
    public void dispose() {
        if (soundEat != null) soundEat.dispose();
        if (soundMove != null) soundMove.dispose();
        if (soundCollision != null) soundCollision.dispose();
    }
}