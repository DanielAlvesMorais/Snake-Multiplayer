package com.POO.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameScreen implements Screen {

    // Referência ao jogo principal
    final SnakeGame game;

    // Entidades do jogo
    private Snake snake1;
    private Snake snake2;
    private Apple apple;
    private TecladoController controller;

    // Controle de tempo do movimento
    private float moveTimer = 0;

    // Texturas da cobra 1
    private Texture snake1HeadTexture;
    private Texture snake1BodyTexture;
    private Texture snake1TailTexture;
    private Texture snake1CornerTexture;

    // Texturas da cobra 2
    private Texture snake2HeadTexture;
    private Texture snake2BodyTexture;
    private Texture snake2TailTexture;
    private Texture snake2CornerTexture;

    // Textura da maçã
    private Texture appleTexture;

    // Fundo opcional
    private Texture backgroundTexture;

    // Posições antigas das caudas para a interpolação fluida
    private float oldTail1X, oldTail1Y;
    private float oldTail2X, oldTail2Y;

    public GameScreen(final SnakeGame game) {
        this.game = game;

        snake1 = new Snake(280, 240);
        snake2 = new Snake(360, 240);

        controller = new TecladoController(snake1, snake2);

        apple = new Apple();
        reposicionarMaca();

        Gdx.input.setInputProcessor(controller);

        // Carrega as texturas

        appleTexture = new Texture("apple.png");

        snake1HeadTexture = new Texture("snake1_head.png");
        snake1BodyTexture = new Texture("snake1_body.png");
        snake1TailTexture = new Texture("snake1_tail.png");
        snake1CornerTexture = new Texture("snake1_corner.png");

        snake2HeadTexture = new Texture("snake2_head.png");
        snake2BodyTexture = new Texture("snake2_body.png");
        snake2TailTexture = new Texture("snake2_tail.png");
        snake2CornerTexture = new Texture("snake2_corner.png");

        // Inicializa as caudas antigas com a posição inicial delas
        oldTail1X = snake1.getBody().peekLast().getX();
        oldTail1Y = snake1.getBody().peekLast().getY();
        oldTail2X = snake2.getBody().peekLast().getX();
        oldTail2Y = snake2.getBody().peekLast().getY();

        // Opcional
        //backgroundTexture = new Texture("background.png");
    }

    @Override
    public void render(float delta) {

        ScreenUtils.clear(0.0f, 0.5f, 0.0f, 1.0f);

        moveTimer += delta;

        // Guarda a cauda antiga ANTES do movimento acontecer
        if (moveTimer > 0.4f) {

            // Guarda a posição atual da cauda da cobra 1
            SnakeBody tail1 = snake1.getBody().peekLast();
            oldTail1X = tail1.getX();
            oldTail1Y = tail1.getY();

            // Guarda a posição atual da cauda da cobra 2
            SnakeBody tail2 = snake2.getBody().peekLast();
            oldTail2X = tail2.getX();
            oldTail2Y = tail2.getY();

            // Só depois move
            snake1.move();
            snake2.move();

            moveTimer = 0;
        }

        // Calcula o progresso do movimento (garantindo o limite máximo de 1.0f)
        float alpha = Math.min(moveTimer / 0.4f, 1.0f);

        // Verifica maçã para cobra 1
        SnakeBody head1 = snake1.getBody().peekFirst();

        if (apple.getX() == head1.getX()
                && apple.getY() == head1.getY()) {

            snake1.eatApple();
            reposicionarMaca();
        }

        // Verifica maçã para cobra 2
        SnakeBody head2 = snake2.getBody().peekFirst();

        if (apple.getX() == head2.getX()
                && apple.getY() == head2.getY()) {

            snake2.eatApple();
            reposicionarMaca();
        }

        // Colisões
        if (snake1.checkCollision(snake2)
                || snake2.checkCollision(snake1)) {

            snake1 = new Snake(280, 240);
            snake2 = new Snake(360, 240);

            controller = new TecladoController(snake1, snake2);

            Gdx.input.setInputProcessor(controller);

            reposicionarMaca();
        }

        game.getBatch().begin();

        // Fundo opcional
        if (backgroundTexture != null) {
            game.getBatch().draw(
                    backgroundTexture,
                    0,
                    0,
                    Gdx.graphics.getWidth(),
                    Gdx.graphics.getHeight()
            );
        }

        // Maçã
        game.getBatch().draw(
                appleTexture,
                apple.getX(),
                apple.getY(),
                20,
                20
        );

        // Cobra 1
        desenharCobra(
                snake1,
                snake1HeadTexture,
                snake1BodyTexture,
                snake1TailTexture,
                snake1CornerTexture,
                alpha,
                oldTail1X,
                oldTail1Y
        );

        // Cobra 2
        desenharCobra(
                snake2,
                snake2HeadTexture,
                snake2BodyTexture,
                snake2TailTexture,
                snake2CornerTexture,
                alpha,
                oldTail2X,
                oldTail2Y
        );

        // Colisões
        if (snake1.checkCollision(snake2) || snake2.checkCollision(snake1)) {
            snake1 = new Snake(280, 240);
            snake2 = new Snake(360, 240);
            controller = new TecladoController(snake1, snake2);
            Gdx.input.setInputProcessor(controller);
            reposicionarMaca();

            // REINICIA AS CAUDAS AQUI TAMBÉM
            oldTail1X = snake1.getBody().peekLast().getX();
            oldTail1Y = snake1.getBody().peekLast().getY();
            oldTail2X = snake2.getBody().peekLast().getX();
            oldTail2Y = snake2.getBody().peekLast().getY();
        }

        game.getBatch().end();
    }

    /**
     * Desenha uma cobra usando cabeça, corpo e cauda.
     */
    /**
     * Desenha uma cobra usando cabeça, corpo e cauda com movimento fluido.
     */
    private void desenharCobra(
            Snake snake,
            Texture headTexture,
            Texture bodyTexture,
            Texture tailTexture,
            Texture cornerTexture,
            float alpha,
            float oldTailX,
            float oldTailY) {

        SnakeBody[] partes = snake.getBody().toArray(new SnakeBody[0]);
        int tamanho = partes.length;

        for (int i = 0; i < tamanho; i++) {
            SnakeBody atual = partes[i];

            Texture textura;
            float angulo = 0;

            // --- CÁLCULO DA POSIÇÃO FLUIDA (INTERPOLAÇÃO) ---
            float oldX, oldY;

            if (i < tamanho - 1) {
                // Para a cabeça e corpo, a posição antiga era a do bloco logo atrás na lista
                oldX = partes[i + 1].getX();
                oldY = partes[i + 1].getY();
            } else {
                // Para a cauda, usamos a posição que guardámos antes do movimento acontecer
                oldX = oldTailX;
                oldY = oldTailY;
            }

            // Descobre a posição visual exata neste frame baseada no alpha
            float deltaX = atual.getX() - oldX;
            float deltaY = atual.getY() - oldY;

            if (Math.abs(deltaX) > 20) {
                deltaX = -Math.signum(deltaX) * 20;
            }

            if (Math.abs(deltaY) > 20) {
                deltaY = -Math.signum(deltaY) * 20;
            }

            float renderX = oldX + deltaX * alpha;
            float renderY = oldY + deltaY * alpha;
            // ------------------------------------------------

            // [A TUA LÓGICA DE DEFINIR TEXTURA E ÂNGULO FICA EXATAMENTE IGUAL]
            // CABEÇA
            if (i == 0) {
                textura = headTexture;
                switch (snake.getDirection()) {
                    case UP:    angulo = 270; break;
                    case DOWN:  angulo = 90;  break;
                    case LEFT:  angulo = 0;   break;
                    case RIGHT: angulo = 180; break;
                }
            }
            // CAUDA
            else if (i == tamanho - 1) {
                textura = tailTexture;
                SnakeBody frente = partes[i - 1];
                int dx = frente.getX() - atual.getX();
                int dy = frente.getY() - atual.getY();

                if (dx > 0)       angulo = 180;
                else if (dx < 0)  angulo = 0;
                else if (dy > 0)  angulo = 270;
                else              angulo = 90;
            }
            // CORPO
            else {
                SnakeBody anterior = partes[i - 1];
                SnakeBody proximo = partes[i + 1];

                int dx1 = anterior.getX() - atual.getX();
                int dy1 = anterior.getY() - atual.getY();
                int dx2 = proximo.getX() - atual.getX();
                int dy2 = proximo.getY() - atual.getY();

                if (dy1 == 0 && dy2 == 0) {
                    textura = bodyTexture;
                    angulo = 0;
                } else if (dx1 == 0 && dx2 == 0) {
                    textura = bodyTexture;
                    angulo = 90;
                } else {
                    textura = cornerTexture;
                    if ((dy1 < 0 && dx2 > 0) || (dx1 > 0 && dy2 < 0)) {
                        angulo = 90;
                    } else if ((dx1 < 0 && dy2 < 0) || (dy1 < 0 && dx2 < 0)) {
                        angulo = 0;
                    } else if ((dy1 > 0 && dx2 < 0) || (dx1 < 0 && dy2 > 0)) {
                        angulo = 270;
                    } else {
                        angulo = 180;
                    }
                }
            }

            // DESENHA USANDO AS COORDENADAS FLUIDAS (renderX e renderY)
            game.getBatch().draw(
                    textura,
                    renderX,        // <--- Alterado aqui
                    renderY,        // <--- Alterado aqui
                    10,
                    10,
                    20,
                    20,
                    1f,
                    1f,
                    angulo,
                    0,
                    0,
                    textura.getWidth(),
                    textura.getHeight(),
                    false,
                    false
            );
        }
    }

    /**
     * Garante que a maçã não apareça sobre nenhuma cobra.
     */
    private void reposicionarMaca() {

        boolean posicaoValida = false;

        while (!posicaoValida) {

            apple = new Apple();
            posicaoValida = true;

            for (SnakeBody pedaco : snake1.getBody()) {

                if (apple.getX() == pedaco.getX()
                        && apple.getY() == pedaco.getY()) {

                    posicaoValida = false;
                    break;
                }
            }

            if (!posicaoValida) {
                continue;
            }

            for (SnakeBody pedaco : snake2.getBody()) {

                if (apple.getX() == pedaco.getX()
                        && apple.getY() == pedaco.getY()) {

                    posicaoValida = false;
                    break;
                }
            }
        }
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

    @Override
    public void dispose() {

        snake1HeadTexture.dispose();
        snake1BodyTexture.dispose();
        snake1TailTexture.dispose();
        snake1CornerTexture.dispose();

        snake2HeadTexture.dispose();
        snake2BodyTexture.dispose();
        snake2TailTexture.dispose();
        snake2CornerTexture.dispose();

        appleTexture.dispose();

        if (backgroundTexture != null) {
            backgroundTexture.dispose();
        }
    }
}