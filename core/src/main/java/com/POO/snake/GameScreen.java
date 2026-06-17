package com.POO.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameScreen implements Screen {

    final SnakeGame game;

    // Entidades do jogo
    private final Snake snake1;
    private final Snake snake2;
    private Apple apple;
    private final TecladoController controller;

    // Orquestradores de Alta Coesão
    private final GameAssets assets;
    private final SnakeRenderer snakeRenderer;
    private final HudRenderer hudRenderer;

    // Controle de tempo do movimento
    private float moveTimer_snake1 = 0;
    private float moveTimer_snake2 = 0;
    private float moveInterval_snake1 = 0.15f;
    private float moveInterval_snake2 = 0.15f;
    private static final float MIN_INTERVAL = 0.025f;
    private static final float SPEED_INCREASE = 0.01f;

    // Tempo de jogo
    private float tempoDeJogo = 0f;

    public GameScreen(final SnakeGame game) {
        this.game = game;

        // Inicializa os renderizadores e assets desacoplados
        this.assets = new GameAssets();
        this.snakeRenderer = new SnakeRenderer(game.getBatch());
        this.hudRenderer = new HudRenderer(game);

        // Inicializa as cobras nas posições iniciais
        this.snake1 = new Snake(280, 240);
        this.snake2 = new Snake(360, 240);
        this.moveInterval_snake1 = 0.15f;
        this.moveInterval_snake2 = 0.15f;

        this.controller = new TecladoController(this, snake1, snake2);

        this.apple = new Apple();
        reposicionarMaca();

        Gdx.input.setInputProcessor(controller);
    }

    private String definirVencedor() {
        boolean snake1Colidiu = snake1.checkCollision(snake2);
        boolean snake2Colidiu = snake2.checkCollision(snake1);

        if (snake1Colidiu && snake2Colidiu) {
            if (snake1.getScore() > snake2.getScore()) {
                return "P1";
            }
            if (snake2.getScore() > snake1.getScore()) {
                return "P2";
            }
            return "Empate";
        }

        if (snake1Colidiu) {
            return "P2";
        }
        if (snake2Colidiu) {
            return "P1";
        }

        return "Empate";
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.0f, 0.5f, 0.0f, 1.0f);

        moveTimer_snake1 += delta;
        moveTimer_snake2 += delta;
        tempoDeJogo += delta;

        if (moveTimer_snake1 > moveInterval_snake1) {
            snake1.move();
            moveTimer_snake1 = 0;
        }
        if (moveTimer_snake2 > moveInterval_snake2) {
            snake2.move();
            moveTimer_snake2 = 0;
        }

        float alpha = Math.min(moveTimer_snake1 / moveInterval_snake1, 1.0f);
        float alpha2 = Math.min(moveTimer_snake2 / moveInterval_snake2, 1.0f);

        // Checagem de colisões com a maçã
        if(verificarColisaoMaca(snake1)) {
            moveInterval_snake1 = Math.max(MIN_INTERVAL, moveInterval_snake1 - SPEED_INCREASE);
        }
        if(verificarColisaoMaca(snake2)) {
            moveInterval_snake2 = Math.max(MIN_INTERVAL, moveInterval_snake2 - SPEED_INCREASE);
        }

        // Verifica colisão crítica estrutural antes do loop de desenho
        if (snake1.checkCollision(snake2) || snake2.checkCollision(snake1)) {
            finalizarPartida();
            return;
        }

// --- DESENHO DA ÁREA DE JOGO ---
        game.getBatch().begin(); // Abre o lote de renderização

        // 1. Desenha o Fundo
        if (assets.backgroundTexture != null) {
            game.getBatch().draw(assets.backgroundTexture, 0, 0, 640, 480);
        }

        // 2. Desenha a Maçã
        game.getBatch().draw(assets.appleTexture, apple.getX(), apple.getY(), 20, 20);

        // 3. Desenha as Cobras delegando ao SnakeRenderer (AGORA DENTRO DO BEGIN/END)
        snakeRenderer.desenhar(snake1, assets.snake1HeadTexture, assets.snake1BodyTexture, assets.snake1TailTexture, assets.snake1CornerTexture, alpha);
        snakeRenderer.desenhar(snake2, assets.snake2HeadTexture, assets.snake2BodyTexture, assets.snake2TailTexture, assets.snake2CornerTexture, alpha);

        game.getBatch().end(); // Fecha o lote de renderização com tudo dentro!

        // 4. Renderiza elementos estéticos de texto via HudRenderer
        // (Não se preocupe com este, o HudRenderer já abre e fecha o próprio batch internamente)
        hudRenderer.desenhar(assets, snake1.getScore(), snake2.getScore(), tempoDeJogo);
    }

    private boolean verificarColisaoMaca(Snake snake) {
        SnakeBody head = snake.getBody().peekFirst();
        if (apple.getX() == head.getX() && apple.getY() == head.getY()) {
            snake.eatApple();
            SoundManager.getInstance().playEat();
            reposicionarMaca();

            return true;
        }
        return false;
    }

    private void reposicionarMaca() {
        boolean posicaoValida = false;
        while (!posicaoValida) {
            apple = new Apple();
            posicaoValida = true;

            for (SnakeBody pedaco : snake1.getBody()) {
                if (apple.getX() == pedaco.getX() && apple.getY() == pedaco.getY()) {
                    posicaoValida = false;
                    break;
                }
            }
            if (!posicaoValida) {
                continue;
            }

            for (SnakeBody pedaco : snake2.getBody()) {
                if (apple.getX() == pedaco.getX() && apple.getY() == pedaco.getY()) {
                    posicaoValida = false;
                    break;
                }
            }
        }
    }

    private void finalizarPartida() {
        String vencedor = definirVencedor();
        SoundManager.getInstance().playCollision();
        int score;
        switch (vencedor) {
            case "P1":
                score = snake1.getScore();
                break;
            case "P2":
                score = snake2.getScore();
                break;
            case "Empate":
                score = Math.max(snake1.getScore(), snake2.getScore());
                break;
            default:
                score = 0;
        }

        game.setScreen(new GameOver(game, vencedor, score, assets.snake1HeadTexture, assets.snake2HeadTexture));
    }

    @Override
    public void show() {
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    public TecladoController getController() {
        return this.controller;
    }

    @Override
    public void dispose() {
        assets.dispose();
        hudRenderer.dispose();
    }
}
