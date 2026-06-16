package com.POO.snake;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SnakeRenderer {

    private final SpriteBatch batch;

    public SnakeRenderer(SpriteBatch batch) {
        this.batch = batch;
    }

    public void desenhar(
            Snake snake,
            Texture headTexture,
            Texture bodyTexture,
            Texture tailTexture,
            Texture cornerTexture,
            float alpha) {

        SnakeBody[] partes = snake.getBody().toArray(new SnakeBody[0]);
        int tamanho = partes.length;

        for (int i = 0; i < tamanho; i++) {
            SnakeBody atual = partes[i];
            Texture textura;
            float angulo = 0;
            float renderX = atual.getX();
            float renderY = atual.getY();

            // Lógica baseada nas posições do SnakeBody
            if (i == 0) {
                textura = headTexture;
                switch (snake.getDirection()) {
                    case UP:    angulo = 270; break;
                    case DOWN:  angulo = 90;  break;
                    case LEFT:  angulo = 0;   break;
                    case RIGHT: angulo = 180; break;
                }
            } else if (i == tamanho - 1) {
                textura = tailTexture;
                SnakeBody frente = partes[i - 1];
                int dx = frente.getX() - atual.getX();
                int dy = frente.getY() - atual.getY();
                if (dx > 0)       angulo = 180;
                else if (dx < 0)  angulo = 0;
                else if (dy > 0)  angulo = 270;
                else              angulo = 90;
            } else {
                SnakeBody anterior = partes[i - 1];
                SnakeBody proximo = partes[i + 1];
                int dx1 = anterior.getX() - atual.getX();
                int dy1 = anterior.getY() - atual.getY();
                int dx2 = proximo.getX() - atual.getX();
                int dy2 = proximo.getY() - atual.getY();

                if (dy1 == 0 && dy2 == 0) {
                    textura = bodyTexture;
                    angulo = 0;
                } else if (dx1 == 0 && dx2 == 0) {
                    textura = bodyTexture;
                    angulo = 90;
                } else {
                    textura = cornerTexture;
                    if ((dy1 < 0 && dx2 > 0) || (dx1 > 0 && dy2 < 0))       angulo = 90;
                    else if ((dx1 < 0 && dy2 < 0) || (dy1 < 0 && dx2 < 0))  angulo = 0;
                    else if ((dy1 > 0 && dx2 < 0) || (dx1 < 0 && dy2 > 0))  angulo = 270;
                    else                                                    angulo = 180;
                }
            }

            batch.draw(
                    textura,
                    renderX, renderY,
                    10, 10,
                    20, 20,
                    1f, 1f,
                    angulo,
                    0, 0,
                    textura.getWidth(), textura.getHeight(),
                    false, false
            );
        }
    }
}