package com.POO.snake;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

public class HudRenderer {

    private final SnakeGame game;
    private final ShapeRenderer shapeRenderer;
    private final GlyphLayout layout;

    public HudRenderer(SnakeGame game) {
        this.game = game;
        this.shapeRenderer = new ShapeRenderer();
        this.layout = new GlyphLayout();
    }

    public void desenhar(GameAssets assets, int score1, int score2, float tempoDeJogo) {
        // --- BARRA PRETA SUPERIOR ---
        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(0, 440, 640, 40);
        shapeRenderer.end();

        // --- TEXTO DA HUD ---
        int segundos = (int) tempoDeJogo % 60;
        int minutos = (int) tempoDeJogo / 60;

        game.getBatch().begin();
        assets.font.setColor(Color.WHITE);
        assets.font.draw(game.getBatch(), "P1: " + score1, 10, 472);
        assets.font.draw(game.getBatch(), "P2: " + score2, 530, 472);
        
        String tempo = String.format("%02d:%02d", minutos, segundos);
        layout.setText(assets.font, tempo);
        assets.font.draw(game.getBatch(), tempo, (640 - layout.width) / 2, 472);
        game.getBatch().end();
    }

    public void dispose() {
        shapeRenderer.dispose();
    }
}