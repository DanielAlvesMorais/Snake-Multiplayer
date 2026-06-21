package com.POO.snake;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * A specialized visual renderer for a snake entity.
 * It analyzes the spatial relationships between adjacent body segments to calculate
 * the correct texture (head, straight body, corner, or tail) and rotation angle 
 * for every part of the snake.
 *
 * @author Davi N. P.
 * @author Daniel A. M.
 * @author Gustavo S. L.
 * @version 1.0
 */
public class SnakeRenderer {

    private final SpriteBatch batch;

    /**
     * Initializes the renderer with the game's shared drawing batch.
     *
     * @param batch The SpriteBatch used to push textures to the GPU.
     */
    public SnakeRenderer(SpriteBatch batch) {
        this.batch = batch;
    }

    /**
     * Evaluates and draws each segment of the snake's body using procedural rotation.
     *
     * @param snake         The logical Snake object to render.
     * @param headTexture   The texture file representing the snake's head.
     * @param bodyTexture   The texture file representing a straight segment.
     * @param tailTexture   The texture file representing the tip of the tail.
     * @param cornerTexture The curved texture file representing a 90-degree turn.
     * @param alpha         The interpolation float (0 to 1) for smooth movement (if applicable).
     */
    public void draw(
            Snake snake,
            Texture headTexture,
            Texture bodyTexture,
            Texture tailTexture,
            Texture cornerTexture,
            float alpha) {

        SnakeBody[] parts = snake.getBody().toArray(new SnakeBody[0]);
        int length = parts.length;

        for (int i = 0; i < length; i++) {
            SnakeBody current = parts[i];
            Texture texture;
            float angle = 0;
            float renderX = current.getX();
            float renderY = current.getY();

            if (i == 0) {
                texture = headTexture;
                switch (snake.getDirection()) {
                    case UP:    angle = 270; break;
                    case DOWN:  angle = 90;  break;
                    case LEFT:  angle = 0;   break;
                    case RIGHT: angle = 180; break;
                }
            } else if (i == length - 1) {
                texture = tailTexture;
                SnakeBody front = parts[i - 1];
                int dx = front.getX() - current.getX();
                int dy = front.getY() - current.getY();
                if (dx > 0)       angle = 180;
                else if (dx < 0)  angle = 0;
                else if (dy > 0)  angle = 270;
                else              angle = 90;
            } else {
                SnakeBody previous = parts[i - 1];
                SnakeBody next = parts[i + 1];
                int dx1 = previous.getX() - current.getX();
                int dy1 = previous.getY() - current.getY();
                int dx2 = next.getX() - current.getX();
                int dy2 = next.getY() - current.getY();

                if (dy1 == 0 && dy2 == 0) {
                    texture = bodyTexture;
                    angle = 0;
                } else if (dx1 == 0 && dx2 == 0) {
                    texture = bodyTexture;
                    angle = 90;
                } else {
                    texture = cornerTexture;
                    if ((dy1 < 0 && dx2 > 0) || (dx1 > 0 && dy2 < 0))       angle = 90;
                    else if ((dx1 < 0 && dy2 < 0) || (dy1 < 0 && dx2 < 0))  angle = 0;
                    else if ((dy1 > 0 && dx2 < 0) || (dx1 < 0 && dy2 > 0))  angle = 270;
                    else                                                    angle = 180;
                }
            }

            batch.draw(
                    texture,
                    renderX, renderY,
                    10, 10,
                    20, 20,
                    1f, 1f,
                    angle,
                    0, 0,
                    texture.getWidth(), texture.getHeight(),
                    false, false
            );
        }
    }
}