package com.POO.snake;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

// Classe que vai ler o teclado herda de InputAdapter para transformar
// a leitura do teclado em um Processo Orientado a Eventos
// InputAdapter implementa a interface InputProcessor
public class TecladoController extends InputAdapter {
    // Variavel pra guardar a cobra
    private Snake snake1;
    private Snake snake2;
    private Menu menu;

    public TecladoController(Snake snake1, Snake snake2) {
        this.snake1 = snake1;
        this.snake2 = snake2;
    }
    public TecladoController(){
    }

    // método que indentifica qual tecla foi pressionada e muda a direção da cobra de acordo
    // Listener
    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.UP:
                snake1.setDirection(Direction.UP);
                return true;
            case Input.Keys.DOWN:
                snake1.setDirection(Direction.DOWN);
                return true;
            case Input.Keys.LEFT:
                snake1.setDirection(Direction.LEFT);
                return true;
            case Input.Keys.RIGHT:
                snake1.setDirection(Direction.RIGHT);
                return true;
            case Input.Keys.W:
                snake2.setDirection(Direction.UP);
                return true;
            case Input.Keys.S:
                snake2.setDirection(Direction.DOWN);    
                return true;
            case Input.Keys.A:
                snake2.setDirection(Direction.LEFT);
                return true;
            case Input.Keys.D:
                snake2.setDirection(Direction.RIGHT);       
                return true;
            case Input.Keys.ENTER:
                menu.enterGame();
                return true;
        }
        return false;
    }
}