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


    // O construtor recebe o jogo principal para termos acesso ao getBatch() (o nosso pincel)
    public GameScreen(final SnakeGame game) {
        // instancia o jogo principal, cobra e o controlador do teclado
        this.game = game;
        this.snake = new Snake();
        this.controller = new TecladoController(this.snake);

        //transforma o controlador de teclado em um Processo Orientado a Eventos
        Gdx.input.setInputProcessor(this.controller);

        // Pixmap pra guardar a texture da cobra no vram
        Pixmap pixmap = new Pixmap(20,20,Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.LIME);
        pixmap.fill();
        // GPU so recebe texture
        this.snakeTexture = new Texture(pixmap);
        pixmap.dispose();

    }

    // Método render é chamado a cada frame, é responsável por desenhar as telas
    @Override
    public void render(float delta) {
        // Pinta o fundo da tela com um verde escuro (simulando a cor de fundo do grid)
        ScreenUtils.clear(0.0f, 0.2f, 0.0f, 1.0f);

        // controla o tempo entre cada movimento da cobra
        moveTimer += delta;
        if(moveTimer > 0.6f){
            snake.move();
            moveTimer = 0;
        }

        // Prepara o pincel para desenhar a cobra no futuro
        game.getBatch().begin();
        
        // for que percorre a fila dupla e a cada body desenha um quadrado verde claro
        for (SnakeBody body : snake.getBody()) {
            // Aqui desenharemos cada parte do corpo da cobra usando o game.getBatch() e as coordenadas de body.getX() e body.getY()
            game.getBatch().draw(snakeTexture, body.getX(), body.getY(), 20, 20);
        }

        // Finaliza o desenho da cobra
        game.getBatch().end();
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
    }
}
