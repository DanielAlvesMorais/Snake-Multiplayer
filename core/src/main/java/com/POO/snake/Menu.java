package com.POO.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.ScreenUtils;

public class Menu extends ScreenAdapter {

    // Variaveis do jogo, da fonte e do input
    private SnakeGame game;
    private BitmapFont font;
    private TecladoController controller;
    private GlyphLayout layout;

    public Menu(SnakeGame game) {
        this.game = game;

        this.controller = new TecladoController(this);
        this.font = new BitmapFont();
        this.layout = new GlyphLayout(); // Inicializa o medidor
        Gdx.input.setInputProcessor(controller);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.NAVY);

        // Largura da tela do jogo (640)
        float larguraTela = Gdx.graphics.getWidth();

        game.getBatch().begin();

        // --- DESENHANDO O TÍTULO ---
        font.setColor(Color.WHITE);
        font.getData().setScale(2f); // Define o tamanho antes de medir
        layout.setText(font, "SNAKE MULTIPLAYER"); // Mede o texto

        // Conta matemática: (Metade da Tela) - (Metade do tamanho do texto)
        float xTitulo = (larguraTela - layout.width) / 2;
        font.draw(game.getBatch(), "SNAKE MULTIPLAYER", xTitulo, 350);

        // --- DESENHANDO O SUBTÍTULO ---
        font.setColor(Color.LIGHT_GRAY);
        font.getData().setScale(1.2f); // Define o tamanho antes de medir
        layout.setText(font, "Pressione Enter para Jogar"); // Mede o texto

        float xSubtitulo = (larguraTela - layout.width) / 2;
        font.draw(game.getBatch(), "Pressione Enter para Jogar", xSubtitulo, 250);

        //---- DESENHANDO A INSTRUÇÃO DE PAUSA
        font.setColor(Color.LIGHT_GRAY);
        font.getData().setScale(1.2f); // Define o tamanho antes de medir
        layout.setText(font, "Pressione P para pausar"); // Mede o texto

        float xpause = (larguraTela - layout.width) / 2;
        font.draw(game.getBatch(), "Pressione P para pausar", xpause, 150);


        game.getBatch().end();
    }

    public void enterGame() {
        game.setScreen(new GameScreen(game));
    }

    @Override
    public void dispose() {
        font.dispose();
    }
}
