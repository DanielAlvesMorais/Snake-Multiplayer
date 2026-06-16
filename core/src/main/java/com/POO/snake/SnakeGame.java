package com.POO.snake;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SnakeGame extends Game {
    private SpriteBatch batch;
    private Rank ranking;

    @Override
    public void create() {
        // Inicializa o batch que todas as telas vão compartilhar para desenhar
        batch = new SpriteBatch();
        
        // Inicializa o sistema de Ranking
        ranking = new Rank();
        
        // Define a primeira tela que vai aparecer quando o jogo abrir (o Menu)
        this.setScreen(new Menu(this));
    }

    // Método essencial: permite que GameScreen, HudRenderer e as outras telas usem o mesmo batch
    public SpriteBatch getBatch() {
        return batch;
    }

    // Método essencial: permite que o GameOver e o RankScreen acessem o ranking salvo
    public Rank getRanking() {
        return ranking;
    }

    @Override
    public void dispose() {
        // limpa o batch da memória ao fechar o jogo
        if (batch != null) {
            batch.dispose();
        }
    }
}