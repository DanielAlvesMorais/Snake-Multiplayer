package com.POO.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;

public class TecladoController extends InputAdapter {

    private Snake snake1;
    private Snake snake2;
    private GameScreen gameScreen; // <-- Nova referência para controlar a pausa

    private Menu menuScreen;
    private Pause pauseScreen;
    private GameOver gameOverScreen;
    private RankScreen rankScreen;

    // Atualizado: Construtor do jogo agora recebe a GameScreen atual
    public TecladoController(GameScreen gameScreen, Snake snake1, Snake snake2) {
        this.gameScreen = gameScreen;
        this.snake1 = snake1;
        this.snake2 = snake2;
    }

    // Construtores específicos para cada tela mapear suas ações
    public TecladoController(Menu menuScreen) { this.menuScreen = menuScreen; }
    public TecladoController(Pause pauseScreen) { this.pauseScreen = pauseScreen; }
    public TecladoController(GameOver gameOverScreen) { this.gameOverScreen = gameOverScreen; }
    public TecladoController(RankScreen rankScreen) { this.rankScreen = rankScreen; }

    @Override
    public boolean keyDown(int keycode) {
        // --- CONTROLES EM JOGO ---
        if (snake1 != null && snake2 != null && gameScreen != null) {
            switch (keycode) {
                // Nova Tecla de Pausa: ESCAPE (ESC)
                case Keys.ESCAPE:
                    // Agora temos acesso ao game através da gameScreen de forma segura!
                    gameScreen.game.setScreen(new Pause(gameScreen.game, gameScreen));
                    return true;

                // Cobra 1 (Setas)
                case Keys.UP:
                    snake1.setDirection(Direction.UP);
                    return true;
                case Keys.DOWN:
                    snake1.setDirection(Direction.DOWN);
                    return true;
                case Keys.LEFT:
                    snake1.setDirection(Direction.LEFT);
                    return true;
                case Keys.RIGHT:
                    snake1.setDirection(Direction.RIGHT);
                    return true;

                // Cobra 2 (WASD)
                case Keys.W:
                    snake2.setDirection(Direction.UP);
                    return true;
                case Keys.S:
                    snake2.setDirection(Direction.DOWN);
                    return true;
                case Keys.A:
                    snake2.setDirection(Direction.LEFT);
                    return true;
                case Keys.D:
                    snake2.setDirection(Direction.RIGHT);
                    return true;
                
                default:
                    return false;
            }
        }

        // --- CONTROLES DO MENU ---
        if (menuScreen != null) {
            switch (keycode) {
                case Keys.ENTER:
                    menuScreen.enterGame();
                    return true;
                case Keys.R:
                    menuScreen.enterRanking();
                    return true;
                default:
                    return false;
            }
        }

        // --- CONTROLES DE PAUSA ---
        if (pauseScreen != null) {
            switch (keycode) {
                case Keys.ENTER:
                    pauseScreen.resumeGame();
                    return true;
                case Keys.ESCAPE:
                    Gdx.app.exit();
                    return true;
                default:
                    return false;
            }
        }

        // --- CONTROLES DE RANKING ---
        if (rankScreen != null) {
            switch (keycode) {
                case Keys.ESCAPE:
                    rankScreen.backToMenu();
                    return true;
                default:
                    return false;
            }
        }

        // --- CONTROLES DE GAME OVER ---
        if (gameOverScreen != null) {
            if (gameOverScreen.WaitingForInitials()) {
                switch (keycode) {
                    case Keys.ENTER:
                        gameOverScreen.confirmarOuRestart();
                        return true;
                    case Keys.BACKSPACE:
                        gameOverScreen.removerUltimaLetra();
                        return true;
                    default:
                        return false;
                }
            } else {
                switch (keycode) {
                    case Keys.ENTER:
                        gameOverScreen.restartGame();
                        return true;
                    case Keys.R:
                        gameOverScreen.viewRanking();
                        return true;
                    case Keys.ESCAPE:
                        Gdx.app.exit();
                        return true;
                    default:
                        return false;
                }
            }
        }

        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        if (gameOverScreen != null && gameOverScreen.WaitingForInitials()) {
            if (Character.isLetter(character)) {
                gameOverScreen.adicionarLetra(character);
                return true;
            }
        }
        return false;
    }
}