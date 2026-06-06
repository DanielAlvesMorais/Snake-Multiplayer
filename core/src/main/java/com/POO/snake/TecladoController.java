package com.POO.snake;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

// Classe que vai ler o teclado herda de InputAdapter para transformar
// a leitura do teclado em um Processo Orientado a Eventos
// InputAdapter implementa a interface InputProcessor
public class TecladoController extends InputAdapter {
    // Variavel pra guardar a cobra
    private Snake snake;
    
    public TecladoController(Snake snake) {
        this.snake = snake;
    }

    // método que indentifica qual tecla foi pressionada e muda a direção da cobra de acordo
    // Listener
    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.UP:
                snake.setDirection(Direction.UP);
                return true;
            case Input.Keys.DOWN:
                snake.setDirection(Direction.DOWN);
                return true;
            case Input.Keys.LEFT:
                snake.setDirection(Direction.LEFT);
                return true;
            case Input.Keys.RIGHT:
                snake.setDirection(Direction.RIGHT);
                return true;
        }
        return false;
    }
}