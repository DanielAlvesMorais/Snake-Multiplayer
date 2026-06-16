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
    private RankScreen rankingScreen;

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

    public TecladoController(RankScreen rankingScreen) {
        this.rankingScreen = rankingScreen;
    }

    // método que indentifica qual tecla foi pressionada e muda a direção da cobra
    // de acordo
    // Listener
    @Override
    public boolean keyDown(int keycode) {

        // --- MODO INPUT DE INICIAIS (GameOver aguardando 3 letras) ---
        if (gameOverScreen != null && gameOverScreen.isAguardandoIniciais()) {
            switch (keycode) {
                case Input.Keys.BACKSPACE:
                    gameOverScreen.removerUltimaLetra();
                    return true;
                case Input.Keys.ENTER:
                    gameOverScreen.confirmarOuRestart();
                    return true;
                default:
                    // Verifica se é uma letra A-Z
                    char letra = (char) keycode;
                    if (letra >= 'A' && letra <= 'Z') {
                            letra = Character.toUpperCase(letra);
                        } else {
                            return false; // Não é uma letra válida, ignora
                        }
                    if (letra != 0) {
                        gameOverScreen.adicionarLetra(letra);
                        return true;
                    }
                    return false;
            }
        }

        switch (keycode) {
            // --- COBRA 1 (Só mexe se a cobra existir) ---
            case Input.Keys.UP:
                if (snake1 != null) {
                    snake1.setDirection(Direction.UP);
                }
                return true;
            case Input.Keys.DOWN:
                if (snake1 != null) {
                    snake1.setDirection(Direction.DOWN);
                }
                return true;
            case Input.Keys.LEFT:
                if (snake1 != null) {
                    snake1.setDirection(Direction.LEFT);
                }
                return true;
            case Input.Keys.RIGHT:
                if (snake1 != null) {
                    snake1.setDirection(Direction.RIGHT);
                }
                return true;

            // --- COBRA 2 (Só mexe se a cobra existir) ---
            case Input.Keys.W:
                if (snake2 != null) {
                    snake2.setDirection(Direction.UP);
                }
                return true;
            case Input.Keys.S:
                if (snake2 != null) {
                    snake2.setDirection(Direction.DOWN);
                }
                return true;
            case Input.Keys.A:
                if (snake2 != null) {
                    snake2.setDirection(Direction.LEFT);
                }
                return true;
            case Input.Keys.D:
                if (snake2 != null) {
                    snake2.setDirection(Direction.RIGHT);
                }
                return true;

            // --- TECLA ENTER ---
            case Input.Keys.ENTER:
                if (menu != null) {
                    menu.enterGame();
                }
                if (gameOverScreen != null) {
                    // ALTERADO: Agora respeita o fluxo de salvar antes de reiniciar!
                    gameOverScreen.confirmarOuRestart();
                }
                if (pauseScreen != null) {
                    pauseScreen.resumeGame();
                }
                return true;

            // --- TECLA ESCAPE ---
            case Input.Keys.ESCAPE:
                if (rankingScreen != null) {
                    rankingScreen.backToMenu();
                } else if (menu != null) {
                    Gdx.app.exit();
                } else if (gameOverScreen != null) {
                    gameOverScreen.returnToMenu();
                }
                return true;

            // --- TECLA R (RANKING) ---
            case Input.Keys.R:
                if (menu != null) {
                    menu.enterRanking();
                } else if (gameOverScreen != null && !gameOverScreen.isAguardandoIniciais()) {
                    gameOverScreen.viewRanking(); // Abre a tela de ranking a partir do GameOver!
                }
                return true;

            default:
                return false;
        }
    }

    
    @Override
    public boolean keyTyped(char character) {
        // Só processa se estiver na tela de GameOver e se estiver esperando texto
        if (gameOverScreen != null && gameOverScreen.isAguardandoIniciais()) {

            // 1. Se for Backspace, apaga a última letra
            if (character == '\b') {
                gameOverScreen.removerUltimaLetra();
                return true;
            }

            // 2. Transforma em maiúscula e verifica se é uma letra válida de A a Z
            char letraMaiuscula = Character.toUpperCase(character);
            if (letraMaiuscula >= 'A' && letraMaiuscula <= 'Z') {
                gameOverScreen.adicionarLetra(letraMaiuscula);
                return true;
            }

            // Se for qualquer outra tecla (setas, números, etc.), consome o evento e não faz nada
            return true;
        }
        return false;
    }
}
