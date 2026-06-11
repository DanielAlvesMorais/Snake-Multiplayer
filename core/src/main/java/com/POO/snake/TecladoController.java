package com.POO.snake;

import com.badlogic.gdx.Gdx;
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
    private GameOver gameOverScreen;
    private Pause pauseScreen;

    public TecladoController(Snake snake1, Snake snake2) {
        this.snake1 = snake1;
        this.snake2 = snake2;
    }

    public TecladoController(Menu menu) {
        this.menu = menu;
    }

    public TecladoController(GameOver gameOver) {
        this.gameOverScreen = gameOver;
    }

    public TecladoController(Pause pauseScreen) {
        this.pauseScreen = pauseScreen;
    }

    // método que indentifica qual tecla foi pressionada e muda a direção da cobra
    // de acordo
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
                if (menu != null) {
                    menu.enterGame();
                }
                if (gameOverScreen != null) {
                    gameOverScreen.restartGame();
                }
                if (pauseScreen != null) {
                    pauseScreen.resumeGame(); // Adicione esta linha

                }
                return true;

            case Input.Keys.ESCAPE:
                if(menu != null || gameOverScreen != null || pauseScreen != null) {
                    Gdx.app.exit(); // Fecha o jogo
                }
                return true;

            case Input.Keys.P:
                // Se o controlador estiver na tela de jogo (onde as cobras existem e não são
                // nulas)
                if (snake1 != null && snake2 != null) {

                }
                return true;
            default:
                return false;
        }
    }
}