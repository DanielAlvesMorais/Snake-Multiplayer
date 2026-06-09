package com.POO.snake;

// Classe SnakeBody guarda as coordenadas de cada parte do corpo
// da cobra
public class SnakeBody{
    private int x;
    private int y;

    // Construtor da Classe recebe as coordenadas x e y
    public SnakeBody(int x, int y) {
        this.x = x;
        this.y =y;
    }
    // Getters para acessar as coordenadas x e y, encapsulamento
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
}
