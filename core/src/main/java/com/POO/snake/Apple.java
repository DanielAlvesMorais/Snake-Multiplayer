package com.POO.snake;

import java.util.Random;

public class Apple {
    
    private int x;
    private int y;

    public Apple(){
        // Instancia um numero aleatorio
        Random random = new Random();
        // Gera um numero entre 0 e 31 e multiplica por 20
        // pra garantir que a maçã apareça em um pixel da cobra
        this.x = random.nextInt(32) * 20;
        // Gera um numero entre 0 e 23 e multiplica por 20
        this.y = random.nextInt(24) * 20;
    }
    // Getters pra acessar as coordenadas
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
}
