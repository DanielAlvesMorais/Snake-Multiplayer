package com.POO.snake;

import java.util.Deque;
import java.util.LinkedList;

// Classe Snake representa a cobra em si, ela utiliza uma fila dupla
// para armazenar as coordenadas de cada parte do corpo, sua movimentação
// será controlada adicionando uma nova cabeça e removendo a cauda
public class Snake {

    // body é o Objeto que instacia a fila dupla
    private Deque<SnakeBody> body = new LinkedList<>();
    // Direction é um Enum pra guardar a direção atual da cobra
    private Direction direction;
    // Verifica se a cobra ja mudou de direção no turno
    private boolean hasChangedDirection = false;
    // Verifica se a cobra comeu a maçã
    private boolean hasEatenApple = false;
    private int score = 0;

    public Snake(int x, int y) {
        // cria a cobra no lugar que for definido e define a direção inicial como UP
        body.addFirst(new SnakeBody(x, y));
        body.add(new SnakeBody(x, y - 20));
        body.add(new SnakeBody(x, y - 40));
        this.direction = Direction.UP;
    }

    // Método para mudar a direção da cobra, abstração 
    public void setDirection(Direction direction) {
        // Não permite mudar de direção mais de uma vez por turno
        if (this.hasChangedDirection) {
            return;
        }
        // não deixa o jogador mudar para a direção oposta
        if ((this.direction == Direction.UP && direction == Direction.DOWN)
                || (this.direction == Direction.DOWN && direction == Direction.UP)
                || (this.direction == Direction.LEFT && direction == Direction.RIGHT)
                || (this.direction == Direction.RIGHT && direction == Direction.LEFT)) {
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
            // ifs checam se a cobra saiu da tela 
            // a tela tem 640x480, a cobra tem 20x20
            // o libGDX tem a origem (0,0) no canto inferior esquerdo
            // a cobra é desenha a partir do pixel inferior esquerdo, 
            // então a cabeça da cobra pode ir até (620, 460) sem sair da tela
            case UP:
                newY += 20;
                if (newY >= 440) {  // era 480
                    newY = 0;
                }
                break;
            case DOWN:
                newY -= 20;
                if (newY < 0) {
                    newY = 440 - 20; // era 460 → agora 420
                }
                break;
            case LEFT:
                newX -= 20;
                if (newX < 0) {
                    newX = 620;
                }
                break;
            case RIGHT:
                newX += 20;
                if (newX >= 640) {
                    newX = 0;
                }
                break;
        }

        // Adiciona a nova cabeça e remove a cauda
        SnakeBody newHead = new SnakeBody(newX, newY, head.getX(), head.getY());
        body.addFirst(newHead);
        // logica pra quando a cobra comer a maçã
        if (this.hasEatenApple) {
            this.hasEatenApple = false;
        } else {
            body.removeLast();
        }
        // a cobra ja andou pode mudar de direção novamente
        this.hasChangedDirection = false;
    }

    // Boolean que seta se a cobra comeu a maçã
    public boolean eatApple() {
        this.hasEatenApple = true;
        this.score++;
        return this.hasEatenApple;
    }

    public int getScore() {
        return this.score;
    }

    // Boolean que seta se a cobra colidiu consigo mesma
    public boolean checkCollision(Snake otherSnake) {
        SnakeBody head = body.peekFirst();
        // verifica se a cobra colidiu nela mesma
        for (SnakeBody part : body) {
            if (part != head && part.getX() == head.getX() && part.getY() == head.getY()) {
                return true;
            }
        }
        // verifica se a cobra colidiu com a outra cobra
        for (SnakeBody part : otherSnake.getBody()) {
            if (part.getX() == head.getX() && part.getY() == head.getY()) {
                return true;
            }
        }
        return false;
    }

    // Método que retorna a fila dupla do corpo da cobra
    public Deque<SnakeBody> getBody() {
        return this.body;
    }

    public Direction getDirection() {
        return this.direction;
    }
}

enum Direction {
    UP, DOWN, LEFT, RIGHT
}
