package com.POO.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.ScreenUtils;

public class Pause extends ScreenAdapter {

    private SnakeGame game;
    private GameScreen gameScreen; // Guarda a tela do jogo em andamento
    private BitmapFont font;
    private GlyphLayout layout;
    private TecladoController controller;

    // Construtor atualizado para receber o jogo principal e a tela de jogo atual
    public Pause(SnakeGame game, GameScreen gameScreen) {
        this.game = game;
        this.gameScreen = gameScreen;
        this.font = new BitmapFont();
        this.layout = new GlyphLayout();

        // Configura o controle para esta tela
        this.controller = new TecladoController(this);
        Gdx.input.setInputProcessor(controller);
    }

    @Override
    public void render(float delta) {
        // Fundo azul bem escuro/cinza translúcido para dar efeito de pausa
        ScreenUtils.clear(0.1f, 0.1f, 0.15f, 1);

        float larguraTela = Gdx.graphics.getWidth();
        game.getBatch().begin();

        // 1. Texto "PAUSE" (Grande e Amarelo ou Laranja para diferenciar do Game Over)
        font.setColor(Color.ORANGE);
        font.getData().setScale(3f);
        layout.setText(font, "PAUSE");
        font.draw(game.getBatch(), "PAUSE", (larguraTela - layout.width) / 2, 400);

        // 2. Texto "Press ENTER to Continue"
        font.setColor(Color.WHITE);
        font.getData().setScale(1.2f);
        layout.setText(font, "Press ENTER to Continue");
        font.draw(game.getBatch(), "Press ENTER to Continue", (larguraTela - layout.width) / 2, 250);

        // 3. Texto "Press ESC to Close"
        font.setColor(Color.LIGHT_GRAY);
        layout.setText(font, "Press ESC to Close");
        font.draw(game.getBatch(), "Press ESC to Close", (larguraTela - layout.width) / 2, 200);

        game.getBatch().end();
    }

    // Método chamado pelo TecladoController quando o Enter for pressionado
    public void resumeGame() {
        // Devolve o processador de inputs para o controlador do jogo (GameScreen)
        Gdx.input.setInputProcessor(gameScreen.getController());
        // Retorna para a mesma instância do jogo que estava pausada
        game.setScreen(gameScreen);
    }

    @Override
    public void dispose() {
        font.dispose();
    }
}
