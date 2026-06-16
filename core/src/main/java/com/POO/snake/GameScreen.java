package com.POO.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameScreen implements Screen {

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

    // Fundo
    private Texture backgroundTexture;

    // Posições antigas das caudas (para interpolação)
    private float oldTail1X, oldTail1Y;
    private float oldTail2X, oldTail2Y;

    // Controle de velocidade
    private float moveInterval = 0.15f;
    private static final float MIN_INTERVAL = 0.05f;
    private static final float SPEED_INCREASE = 0.005f;

    // HUD: fonte, barra superior e tempo de jogo
    private BitmapFont font;
    private ShapeRenderer shapeRenderer;
    private float tempoDeJogo = 0f;
    private GlyphLayout layout;

    public GameScreen(final SnakeGame game) {
        this.game = game;

        // Inicializa as cobras nas posições iniciais
        snake1 = new Snake(280, 240);
        snake2 = new Snake(360, 240);
        moveInterval = 0.15f;

        controller = new TecladoController(snake1, snake2);

        apple = new Apple();
        reposicionarMaca();

        Gdx.input.setInputProcessor(controller);

        // Carrega as texturas
        appleTexture = new Texture("New Piskel (21).png");

        snake1HeadTexture = new Texture("snake2_head.png");
        snake1BodyTexture = new Texture("snake2_body.png");
        snake1TailTexture = new Texture("snake2_tail.png");
        snake1CornerTexture = new Texture("snake2_corner.png");

        snake2HeadTexture = new Texture("New Piskel (18).png");
        snake2BodyTexture = new Texture("New Piskel (14).png");
        snake2TailTexture = new Texture("New Piskel (15).png");
        snake2CornerTexture = new Texture("New Piskel (19).png");

        // Guarda a posição inicial das caudas para a interpolação
        oldTail1X = snake1.getBody().peekLast().getX();
        oldTail1Y = snake1.getBody().peekLast().getY();
        oldTail2X = snake2.getBody().peekLast().getX();
        oldTail2Y = snake2.getBody().peekLast().getY();

        backgroundTexture = new Texture("New Piskel (20).png");

        // Inicializa fonte, shapeRenderer e layout para a HUD
        font = new BitmapFont();
        font.getData().setScale(1.5f);
        shapeRenderer = new ShapeRenderer();
        layout = new GlyphLayout();
    }

    // Define quem venceu com base nas colisões e scores
    private String definirVencedor() {
        boolean snake1Colidiu = snake1.checkCollision(snake2);
        boolean snake2Colidiu = snake2.checkCollision(snake1);

        // Colisão simultânea: desempata por score
        if (snake1Colidiu && snake2Colidiu) {
            if (snake1.getScore() > snake2.getScore()) {
                return "P1";
            }
            if (snake2.getScore() > snake1.getScore()) {
                return "P2";
            }
            return "Empate";
        }

        // Só uma colidiu: a outra vence
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

        // Abre a tela de pausa ao pressionar P
        if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.P)) {
            game.setScreen(new Pause(game, this));
        }

        moveTimer += delta;

        // Atualiza o tempo de jogo para exibir na HUD
        tempoDeJogo += delta;
        int segundos = (int) tempoDeJogo % 60;
        int minutos = (int) tempoDeJogo / 60;

        // Move as cobras a cada moveInterval segundos
        if (moveTimer > moveInterval) {

            // Salva a posição da cauda antes de mover (para interpolação)
            SnakeBody tail1 = snake1.getBody().peekLast();
            oldTail1X = tail1.getX();
            oldTail1Y = tail1.getY();

            SnakeBody tail2 = snake2.getBody().peekLast();
            oldTail2X = tail2.getX();
            oldTail2Y = tail2.getY();

            snake1.move();
            snake2.move();

            moveTimer = 0;
        }

        // Alpha para interpolação visual do movimento
        float alpha = Math.min(moveTimer / moveInterval, 1.0f);

        // Verifica se a cobra 1 comeu a maçã
        SnakeBody head1 = snake1.getBody().peekFirst();
        if (apple.getX() == head1.getX() && apple.getY() == head1.getY()) {
            snake1.eatApple();
            moveInterval = Math.max(MIN_INTERVAL, moveInterval - SPEED_INCREASE);
            reposicionarMaca();
        }

        // Verifica se a cobra 2 comeu a maçã
        SnakeBody head2 = snake2.getBody().peekFirst();
        if (apple.getX() == head2.getX() && apple.getY() == head2.getY()) {
            snake2.eatApple();
            moveInterval = Math.max(MIN_INTERVAL, moveInterval - SPEED_INCREASE);
            reposicionarMaca();
        }

        // Verifica colisão antes de desenhar: passa o vencedor e as texturas das cabeças
        if (snake1.checkCollision(snake2) || snake2.checkCollision(snake1)) {
            finalizarPartida();
        }

        // --- DESENHO DA ÁREA DE JOGO ---
        game.getBatch().begin();

        // Fundo limitado à área de jogo (abaixo da barra preta)
        if (backgroundTexture != null) {
            game.getBatch().draw(backgroundTexture, 0, 0, 640, 440);
        }

        // Maçã
        game.getBatch().draw(appleTexture, apple.getX(), apple.getY(), 20, 20);

        // Cobra 1
        desenharCobra(
                snake1,
                snake1HeadTexture, snake1BodyTexture,
                snake1TailTexture, snake1CornerTexture,
                alpha, oldTail1X, oldTail1Y
        );

        // Cobra 2
        desenharCobra(
                snake2,
                snake2HeadTexture, snake2BodyTexture,
                snake2TailTexture, snake2CornerTexture,
                alpha, oldTail2X, oldTail2Y
        );

        // Segunda verificação de colisão (pós-desenho, garante que a tela muda mesmo no frame do impacto)
        if (snake1.checkCollision(snake2) || snake2.checkCollision(snake1)) {
            String vencedor = definirVencedor();
            int score = vencedor.equals("P2") ? snake2.getScore() : snake1.getScore();

            // CORREÇÃO: Passando o 'score' aqui também
            game.setScreen(new GameOver(game, vencedor, score, snake1HeadTexture, snake2HeadTexture));
        }

        game.getBatch().end();

        // --- BARRA PRETA SUPERIOR (desenhada por cima do fundo) ---
        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.setColor(Color.BLACK);
        shapeRenderer.rect(0, 440, 640, 40);
        shapeRenderer.end();

        // --- TEXTO DA HUD (score e tempo) ---
        game.getBatch().begin();
        font.setColor(Color.WHITE);
        font.getData().setScale(1.2f);
        font.draw(game.getBatch(), "P1: " + snake1.getScore(), 10, 472);
        font.draw(game.getBatch(), "P2: " + snake2.getScore(), 530, 472);
        String tempo = String.format("%02d:%02d", minutos, segundos);
        layout.setText(font, tempo);
        font.draw(game.getBatch(), tempo, (640 - layout.width) / 2, 472);
        game.getBatch().end();
    }

    // Desenha uma cobra completa: cabeça, corpo, curvas e cauda com rotação correta
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
            float renderX = atual.getX();
            float renderY = atual.getY();

            // CABEÇA: rotação baseada na direção atual da cobra
            if (i == 0) {
                textura = headTexture;
                switch (snake.getDirection()) {
                    case UP:
                        angulo = 270;
                        break;
                    case DOWN:
                        angulo = 90;
                        break;
                    case LEFT:
                        angulo = 0;
                        break;
                    case RIGHT:
                        angulo = 180;
                        break;
                }
            } // CAUDA: rotação baseada na direção da peça à frente dela
            else if (i == tamanho - 1) {
                textura = tailTexture;
                SnakeBody frente = partes[i - 1];
                int dx = frente.getX() - atual.getX();
                int dy = frente.getY() - atual.getY();
                if (dx > 0) {
                    angulo = 180;
                } else if (dx < 0) {
                    angulo = 0;
                } else if (dy > 0) {
                    angulo = 270;
                } else {
                    angulo = 90;
                }
            } // CORPO: reto ou curva dependendo das peças vizinhas
            else {
                SnakeBody anterior = partes[i - 1];
                SnakeBody proximo = partes[i + 1];
                int dx1 = anterior.getX() - atual.getX();
                int dy1 = anterior.getY() - atual.getY();
                int dx2 = proximo.getX() - atual.getX();
                int dy2 = proximo.getY() - atual.getY();

                // Corpo reto horizontal
                if (dy1 == 0 && dy2 == 0) {
                    textura = bodyTexture;
                    angulo = 0;
                } // Corpo reto vertical
                else if (dx1 == 0 && dx2 == 0) {
                    textura = bodyTexture;
                    angulo = 90;
                } // Curva: determina o ângulo pela combinação de direções
                else {
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

            game.getBatch().draw(
                    textura,
                    renderX, renderY,
                    10, 10,
                    20, 20,
                    1f, 1f,
                    angulo,
                    0, 0,
                    textura.getWidth(), textura.getHeight(),
                    false, false
            );
        }
    }

    // Reposiciona a maçã garantindo que não apareça sobre nenhuma cobra
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

    private void finalizarPartida() {
        String vencedor = definirVencedor();
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

        // CORREÇÃO: Passando o 'score' como 3º argumento antes das texturas!
        game.setScreen(new GameOver(game, vencedor, score, snake1HeadTexture, snake2HeadTexture));
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
        font.dispose();
        shapeRenderer.dispose();

        if (backgroundTexture != null) {
            backgroundTexture.dispose();
        }
    }
}
