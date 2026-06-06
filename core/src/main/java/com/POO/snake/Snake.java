package com.POO.snake;

import java.util.Deque;
import java.util.LinkedList;

// Classe Snake representa a cobra em si, ela utiliza uma fila dupla
// para armazenar as coordenadas de cada parte do corpo, sua movimentação
// será controlada adicionando uma nova cabeça e removendo a cauda

public class Snake{

    // body é o Objeto que instacia a fila dupla
    private Deque<SnakeBody> body = new LinkedList<>();
    // Direction é um Enum pra guardar a direção atual da cobra
    private Direction direction;
    // Verifica se a cobra ja mudou de direção no turno
    private boolean hasChangedDirection = false;
    
    public Snake() {
        // cria o a cabeça da cobra no centro da tela (320, 240) e define a direção inicial como UP
        body.addFirst(new SnakeBody(320, 240));
        body.add(new SnakeBody(320, 220));
        body.add(new SnakeBody(320, 200));
        this.direction = Direction.UP;
    }

    // Método para mudar a direção da cobra, abstração 
    public void setDirection(Direction direction) {
        // Não permite mudar de direção mais de uma vez por turno
        if(this.hasChangedDirection) {
            return;
        }
        // não deixa o jogador mudar para a direção oposta
        if((this.direction == Direction.UP && direction == Direction.DOWN) ||
           (this.direction == Direction.DOWN && direction == Direction.UP) ||
           (this.direction == Direction.LEFT && direction == Direction.RIGHT) ||
           (this.direction == Direction.RIGHT && direction == Direction.LEFT)) {
            return;
        }
        // muda a direção da cobra
        this.direction = direction;
        this.hasChangedDirection = true;
    }   
    // Método responsável pela movimentação
    public void move() {
        // Pega as coordenadas da cabeça atual
        SnakeBody head = body.peekFirst();
        int newX = head.getX();
        int newY = head.getY();
        
        // Calcula as novas coordenadas da cabeça com base na direção atual
        switch (this.direction) {
            case UP: 
                newY += 20; 
                break;
            case DOWN: 
                newY -= 20; 
                break;
            case LEFT: 
                newX -= 20; 
                break;
            case RIGHT: 
                newX += 20; 
                break;
        }

        // Adiciona a nova cabeça e remove a cauda
        SnakeBody newHead = new SnakeBody(newX, newY);
        body.addFirst(newHead);
        body.removeLast();
        // a cobra ja andou pode mudar de direção novamente
        this.hasChangedDirection = false;
    }
    
    // Método que retorna a fila dupla do corpo da cobra
    public Deque<SnakeBody> getBody() {
        return this.body;
    }
}

enum Direction {
    UP, DOWN, LEFT, RIGHT
}