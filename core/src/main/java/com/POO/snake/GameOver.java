package com.POO.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameOver extends ScreenAdapter {

    private SnakeGame game;
    private BitmapFont font;
    private GlyphLayout layout;
    private TecladoController controller;

    public GameOver(SnakeGame game) {
        this.game = game;
        this.font = new BitmapFont();
        this.layout = new GlyphLayout();

        // Configura o controle para esta tela
        this.controller = new TecladoController(this);
        Gdx.input.setInputProcessor(controller);
    }

    @Override
    public void render(float delta) {
        // Fundo vermelho bem escuro
        ScreenUtils.clear(0.2f, 0, 0, 1);

        float larguraTela = Gdx.graphics.getWidth();
        game.getBatch().begin();

        // 1. Texto "GAME OVER" (Grande e Vermelho)
        font.setColor(Color.RED);
        font.getData().setScale(3f);
        layout.setText(font, "GAME OVER");
        font.draw(game.getBatch(), "GAME OVER", (larguraTela - layout.width) / 2, 400);

        // 2. Texto "Press ENTER to Play Again"
        font.setColor(Color.WHITE);
        font.getData().setScale(1.2f);
        layout.setText(font, "Press ENTER to Play Again");
        font.draw(game.getBatch(), "Press ENTER to Play Again", (larguraTela - layout.width) / 2, 250);

        // 3. Texto "Press ESC to Close"
        font.setColor(Color.LIGHT_GRAY);
        layout.setText(font, "Press ESC to Close");
        font.draw(game.getBatch(), "Press ESC to Close", (larguraTela - layout.width) / 2, 200);

        game.getBatch().end();
    }

    public void restartGame() {
        // Volta para a tela de jogo
        game.setScreen(new GameScreen(game));
    }

    @Override
    public void dispose() {
        font.dispose();
    }
}
