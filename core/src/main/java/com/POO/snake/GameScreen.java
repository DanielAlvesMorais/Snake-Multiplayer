package com.POO.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameScreen implements Screen {

    // Objeto que instacia o da classe principal para rodar o jogo
    final SnakeGame game;
    // Objeto para instaciar a cobra
    private Snake snake;
    // textura inicial pra desenhar a cobra
    private Texture snakeTexture;
    // Variavel pra calcular o tempo entre cada movimento da cobra
    private float moveTimer = 0;
    // Variavel pra intanciar o controlador do teclado
    private TecladoController controller;
    // Variavel pra instanciar a maçã
    private Apple apple;
    // Textura inicial da maçã
    private Texture appleTexture;


    // O construtor recebe o jogo principal para termos acesso ao getBatch() (o nosso pincel)
    public GameScreen(final SnakeGame game) {
        // instancia o jogo principal, cobra, o controlador do teclado e a maçã
        this.game = game;
        this.snake = new Snake();
        this.controller = new TecladoController(this.snake);
        this.apple = new Apple();
        //garante que a maçã não nasça em cima da cobra
        reposicionarMaca();

        //transforma o controlador de teclado em um Processo Orientado a Eventos
        Gdx.input.setInputProcessor(this.controller);

        // Pixmap pra guardar a texture da cobra no vram
        Pixmap snakePixmap = new Pixmap(20,20,Pixmap.Format.RGBA8888);
        snakePixmap.setColor(Color.LIME);
        snakePixmap.fill();
        // GPU so recebe texture
        this.snakeTexture = new Texture(snakePixmap);
        snakePixmap.dispose();

        // Pixmap pra guardar a texture da maçã no vram
        Pixmap applePixmap = new Pixmap(20,20,Pixmap.Format.RGBA8888);
        applePixmap.setColor(Color.RED);
        applePixmap.fill();
        this.appleTexture = new Texture(applePixmap);
        applePixmap.dispose();
    }

    // Método render é chamado a cada frame, é responsável por desenhar as telas
    @Override
    public void render(float delta) {
        // Pinta o fundo da tela com um verde escuro (simulando a cor de fundo do grid)
        ScreenUtils.clear(0.0f, 0.2f, 0.0f, 1.0f);

        // controla o tempo entre cada movimento da cobra
        moveTimer += delta;
        if(moveTimer > 0.4f){
            snake.move();
            moveTimer = 0;
        }
        // verifica se a cobra comeu a maçã e spawna uma nova
        SnakeBody head = snake.getBody().peekFirst();
        if(apple.getX() == head.getX() && apple.getY() == head.getY()){
            snake.eatApple();
            reposicionarMaca();
        }
        // Se a cobra colidir consigo mesma, reinicia o jogo
        if (snake.checkCollision()) {
            // instancia uma nova cobra, um novo controlador e reposiciona a maçã
            snake = new Snake();
            this.controller = new TecladoController(this.snake);
            Gdx.input.setInputProcessor(this.controller);
            reposicionarMaca();
        }
        // Prepara o pincel para desenhar a cobra no futuro
        game.getBatch().begin();
        // Desenha a maçã nas coordenadas aleatorias
        game.getBatch().draw(appleTexture, apple.getX(), apple.getY(), 20, 20);
        // for que percorre a fila dupla e a cada body desenha um quadrado verde claro
        for (SnakeBody body : snake.getBody()) {
            // Aqui desenharemos cada parte do corpo da cobra usando o game.getBatch() e as coordenadas de body.getX() e body.getY()
            game.getBatch().draw(snakeTexture, body.getX(), body.getY(), 20, 20);
        }

        // Finaliza o desenho da cobra
        game.getBatch().end();
    }
    // Método que garante que a maçã só nasce em um lugar vazio
    private void reposicionarMaca() {
        boolean posicaoValida = false;
        
        // Continua a tentar enquanto a posição não for válida
        while (!posicaoValida) {
            apple = new Apple(); // A maçã escolhe um X e Y aleatórios
            posicaoValida = true; // Assumimos que a posição é boa, até provar o contrário...
            
            // ...e agora testamos contra TODOS os blocos do corpo da cobra
            for (SnakeBody pedaco : snake.getBody()) {
                if (apple.getX() == pedaco.getX() && apple.getY() == pedaco.getY()) {
                    // Se o X e Y da maçã baterem com qualquer pedaço da cobra, é inválido!
                    posicaoValida = false; 
                    break; // Para o 'for' na mesma hora e faz o 'while' rodar de novo
                }
            }
        }
    }

    // Métodos obrigatórios da interface Screen (mesmo que fiquem vazios por enquanto)
    @Override
    public void show() { }

    @Override
    public void resize(int width, int height) { }

    @Override
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void hide() { }

    @Override
    public void dispose() { 
        snakeTexture.dispose();
        appleTexture.dispose();
    }
}
