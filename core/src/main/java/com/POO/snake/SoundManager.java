package com.POO.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class SoundManager {
    
    private static SoundManager instance;

    private Sound soundEat;
    private Sound soundCollision;
    private Sound soundMove;

    private SoundManager(){
        this.soundEat = Gdx.audio.newSound(Gdx.files.internal("570636__bsp7176__food.wav"));
        this.soundMove = Gdx.audio.newSound(Gdx.files.internal("570635__bsp7176__move.wav"));
        this.soundCollision = Gdx.audio.newSound(Gdx.files.internal("explosion.wav"));
    }

    public static SoundManager getInstance(){
        if(instance == null){
            instance = new SoundManager();
        }
        return instance;
    }
    public void playEat(){
        soundEat.play();
    }
    public void playMove(){
        soundMove.play();
    }public void playCollision(){
        soundCollision.play();
    }

    public void dispose() {
        if (soundEat != null) soundEat.dispose();
        if (soundMove != null) soundMove.dispose();
        if (soundCollision != null) soundCollision.dispose();
    }

}
