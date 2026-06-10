package com.POO.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.utils.ScreenUtils;

public class Menu extends ScreenAdapter{
    // Variaveis do jogo, da fonte e do input
    private SnakeGame game;
    private BitmapFont font;
    private TecladoController controller;

    public Menu(SnakeGame game){
        this.game = game;

        this.controller = new TecladoController();
        this.font = new BitmapFont();
        this.font.getData().setScale(2f); // dobra o tamnanho das letras
        Gdx.input.setInputProcessor(controller);
    }
    @Override
    public void render(float delta){
        // limpa a tela com uma tela preta
        ScreenUtils.clear(Color.NAVY);

        //Desenha os textos
        game.getBatch().begin();

        // Desnha o titulo
        font.setColor(Color.WHITE);
        font.draw(game.getBatch(), "SNAKE MULTIPLAYER", 240, 350);

        font.setColor(Color.LIGHT_GRAY);
        font.getData().setScale(1.2f);
        font.draw(game.getBatch(), "Pressione Enter para Jogar", 240, 250);
        font.getData().setScale(2f);

        game.getBatch().end();

    }
    public void enterGame(){
        game.setScreen(new GameScreen(game));
    }
    @Override
    public void dispose(){
        font.dispose();
    }


}
