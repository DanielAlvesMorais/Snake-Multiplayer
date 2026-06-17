package com.POO.snake;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.ScreenUtils;

public class RankScreen extends ScreenAdapter {

    private SnakeGame game;
    private BitmapFont font;
    private GlyphLayout layout;
    private TecladoController controller;

    public RankScreen(SnakeGame game) {
        this.game = game;
        this.font = new BitmapFont();
        this.layout = new GlyphLayout();

        // Passa a própria instância da tela para o controller
        this.controller = new TecladoController(this);
        Gdx.input.setInputProcessor(controller);
    }

    @Override
    public void render(float delta) {
        // Fundo azul escuro
        ScreenUtils.clear(0f, 0f, 0.2f, 1);

        float larguraTela = Gdx.graphics.getWidth();

        game.getBatch().begin();

        // --- TÍTULO ---
        font.getData().setScale(2.5f);
        font.setColor(Color.WHITE);
        layout.setText(font, "RANKING DE PONTOS");
        font.draw(game.getBatch(), "RANKING DE PONTOS", (larguraTela - layout.width) / 2, 420);

        // --- RENDERIZAÇÃO DA LISTA DO CONTEXTO NÃO-ESTÁTICO ---
        font.getData().setScale(1.5f);
        font.setColor(Color.YELLOW);

        // Pegamos a lista a partir do método não-estático da instância do game!
        ArrayList<JogadorScore> lista = game.getRanking().getListaRanking();

        float yPosicao = 340;
        if (lista.isEmpty()) {
            layout.setText(font, "Nenhum recorde registrado ainda!");
            font.draw(game.getBatch(), "Nenhum recorde registrado ainda!", (larguraTela - layout.width) / 2, yPosicao);
        } else {
            for (int i = 0; i < Math.min(lista.size(), 5); i++) {
                JogadorScore jogador = lista.get(i);
                String linhaText = (i + 1) + ". " + jogador.getNome() + " - " + jogador.getPoints() + " pts";
                
                layout.setText(font, linhaText);
                font.draw(game.getBatch(), linhaText, (larguraTela - layout.width) / 2, yPosicao);
                yPosicao -= 40; // Desce a linha para o próximo jogador
            }
        }

        // Instala uma instrução de retorno na HUD
        font.getData().setScale(1f);
        font.setColor(Color.LIGHT_GRAY);
        layout.setText(font, "Pressione ESC para voltar ao Menu");
        font.draw(game.getBatch(), "Pressione ESC para voltar ao Menu", (larguraTela - layout.width) / 2, 50);

        game.getBatch().end();
    }

    public void backToMenu() {
        game.setScreen(new Menu(game));
    }

    @Override
    public void dispose() {
        font.dispose();
    }
}