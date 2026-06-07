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
    private Snake snake1;
    // Objeto para instaciar a segunda cobra
    private Snake snake2;
    // textura inicial pra desenhar a cobra verde
    private Texture snake1Texture;
    // textura inicial pra desenhar a cobra azul
    private Texture snake2Texture;
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
        this.snake1 = new Snake(280, 240);
        this.snake2 = new Snake(360, 240);
        this.controller = new TecladoController(this.snake1, this.snake2);
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
        this.snake1Texture = new Texture(snakePixmap);
        snakePixmap.dispose();

        Pixmap snake2Pixmap = new Pixmap(20,20,Pixmap.Format.RGBA8888);
        snake2Pixmap.setColor(Color.BLUE);
        snake2Pixmap.fill();
        // GPU so recebe texture
        this.snake2Texture = new Texture(snake2Pixmap);
        snake2Pixmap.dispose();

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
        if(moveTimer > 0.2f){
            snake1.move();
            snake2.move();
            moveTimer = 0;
        }
        // verifica se a cobra comeu a maçã e spawna uma nova
        SnakeBody head1 = snake1.getBody().peekFirst();
        if(apple.getX() == head1.getX() && apple.getY() == head1.getY()){
            snake1.eatApple();
            reposicionarMaca();
        }
        SnakeBody head2 = snake2.getBody().peekFirst();
        if(apple.getX() == head2.getX() && apple.getY() == head2.getY()){
            snake2.eatApple();
            reposicionarMaca();
        }
        // Se a cobra colidir consigo mesma, reinicia o jogo
        if (snake1.checkCollision(snake2) || snake2.checkCollision(snake1)) {
            // instancia uma nova cobra, um novo controlador e reposiciona a maçã
            this.snake1 = new Snake(280, 240);
            this.snake2 = new Snake(360, 240);
            this.controller = new TecladoController(this.snake1, this.snake2);
            Gdx.input.setInputProcessor(this.controller);
            reposicionarMaca();
        }
        // Prepara o pincel para desenhar a cobra no futuro
        game.getBatch().begin();
        // Desenha a maçã nas coordenadas aleatorias
        game.getBatch().draw(appleTexture, apple.getX(), apple.getY(), 20, 20);
        // for que percorre a fila dupla e a cada body desenha um quadrado verde claro
        for (SnakeBody body : snake1.getBody()) {
            // Aqui desenharemos cada parte do corpo da cobra usando o game.getBatch() e as coordenadas de body.getX() e body.getY()
            game.getBatch().draw(snake1Texture, body.getX(), body.getY(), 20, 20);
        }
        for (SnakeBody body : snake2.getBody()) {
            // Aqui desenharemos cada parte do corpo da cobra usando o game.getBatch() e as coordenadas de body.getX() e body.getY()
            game.getBatch().draw(snake2Texture, body.getX(), body.getY(), 20, 20);
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
            for (SnakeBody pedaco : snake1.getBody()) {
                if (apple.getX() == pedaco.getX() && apple.getY() == pedaco.getY()) {
                    // Se o X e Y da maçã baterem com qualquer pedaço da cobra, é inválido!
                    posicaoValida = false; 
                    break; // Para o 'for' na mesma hora e faz o 'while' rodar de novo
                }
            }
            for (SnakeBody pedaco : snake2.getBody()) {
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
        snake1Texture.dispose();
        snake2Texture.dispose();
        appleTexture.dispose();
    }
}
