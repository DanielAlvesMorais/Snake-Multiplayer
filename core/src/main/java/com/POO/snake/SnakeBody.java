package com.POO.snake;

// Classe SnakeBody guarda as coordenadas atuais e anteriores
// de cada parte do corpo da cobra
public class SnakeBody {

    private int x;
    private int y;

    private int previousX;
    private int previousY;

    public SnakeBody(int x, int y) {
        this.x = x;
        this.y = y;

        this.previousX = x;
        this.previousY = y;
    }

    public SnakeBody(int x, int y, int previousX, int previousY) {
        this.x = x;
        this.y = y;

        this.previousX = previousX;
        this.previousY = previousY;
    }

    public void savePreviousPosition() {
        this.previousX = this.x;
        this.previousY = this.y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getPreviousX() {
        return previousX;
    }

    public int getPreviousY() {
        return previousY;
    }
}