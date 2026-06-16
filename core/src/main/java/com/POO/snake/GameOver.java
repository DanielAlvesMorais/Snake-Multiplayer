package com.POO.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameOver extends ScreenAdapter {

    private SnakeGame game;
    private BitmapFont font;
    private GlyphLayout layout;
    private TecladoController controller;

    private String vencedor;
    private int scoreFinal;
    private Texture headP1;
    private Texture headP2;

    private static final int SPRITE_SIZE = 80;
    private static final int SCORE_MINIMO_RANK = 3; // Pontuação mínima

    private StringBuilder iniciais = new StringBuilder();
    private boolean aguardandoIniciais;

    public GameOver(SnakeGame game, String vencedor, int scoreFinal, Texture headP1, Texture headP2) {
        this.game = game;
        this.vencedor = vencedor;
        this.scoreFinal = scoreFinal;
        this.headP1 = headP1;
        this.headP2 = headP2;
        this.font = new BitmapFont();
        this.layout = new GlyphLayout();

        // Só pede iniciais se não for empate e atingir a pontuação mínima
        this.aguardandoIniciais = !vencedor.equals("Empate") && scoreFinal >= SCORE_MINIMO_RANK;

        this.controller = new TecladoController(this);
        Gdx.input.setInputProcessor(controller);
    }

    @Override
    public void render(float delta) {
        // Fundo verde escuro para contrastar com o jogo
        ScreenUtils.clear(0f, 0.2f, 0f, 1);
        float larguraTela = Gdx.graphics.getWidth();

        game.getBatch().begin();

        // 1. DESENHA O VENCEDOR OU EMPATE
        font.getData().setScale(2.5f);
        if (vencedor.equals("Empate")) {
            font.setColor(Color.YELLOW);
            desenharTextoCentralizado("EMPATE!", 400, larguraTela);

            // Desenha as duas cabecinhas lado a lado
            game.getBatch().draw(headP1, (larguraTela / 2) - 100, 280, SPRITE_SIZE, SPRITE_SIZE);
            game.getBatch().draw(headP2, (larguraTela / 2) + 20, 280, SPRITE_SIZE, SPRITE_SIZE);
        } else {
            font.setColor(Color.GREEN);
            desenharTextoCentralizado(vencedor + " VENCEU!", 400, larguraTela);

            // Desenha a cabeça do vencedor no centro
            Texture headVencedor = vencedor.equals("P1") ? headP1 : headP2;
            game.getBatch().draw(headVencedor, (larguraTela - SPRITE_SIZE) / 2, 280, SPRITE_SIZE, SPRITE_SIZE);
        }

        // 2. DESENHA O SCORE DO VENCEDOR
        font.getData().setScale(1.5f);
        font.setColor(Color.WHITE);
        desenharTextoCentralizado("Pontuacao: " + scoreFinal + " pts", 240, larguraTela);

        // 3. SE COUBER NO RANKING, MOSTRA O INPUT. SE NÃO, MOSTRA AS INSTRUÇÕES NORMAIS
        if (aguardandoIniciais) {
            font.getData().setScale(1.2f);
            font.setColor(Color.CYAN);
            desenharTextoCentralizado("Voce entrou no Ranking! Digite 3 iniciais:", 180, larguraTela);

            // Deixa visível o que ele está digitando de forma fixa (ex: A _ _)
            String textoIniciais = iniciais.toString();
            while (textoIniciais.length() < 3) {
                textoIniciais += " _";
            }

            font.getData().setScale(2.5f);
            font.setColor(Color.WHITE);
            desenharTextoCentralizado(textoIniciais.toUpperCase(), 130, larguraTela);
        } else {
            // Instruções normais de navegação
            font.getData().setScale(1.1f);
            font.setColor(Color.WHITE);
            desenharTextoCentralizado("Pressione ENTER para Jogar Novamente", 160, larguraTela);
            font.setColor(Color.LIGHT_GRAY);
            desenharTextoCentralizado("Pressione R para ver o Ranking", 120, larguraTela);
            desenharTextoCentralizado("Pressione ESC para fechar o jogo", 80, larguraTela);
        }

        game.getBatch().end();
    }

    // Método auxiliar para não ter que ficar repetindo conta matemática de centralizar texto toda hora
    private void desenharTextoCentralizado(String texto, float y, float larguraTela) {
        layout.setText(font, texto);
        font.draw(game.getBatch(), texto, (larguraTela - layout.width) / 2, y);
    }

    // Métodos de controle acionados pelo TecladoController
    public void adicionarLetra(char letra) {
        // Só adiciona se ainda não atingiu o limite de 3 caracteres
        if (aguardandoIniciais && iniciais.length() < 3) {
            iniciais.append(Character.toUpperCase(letra));
        }
    }

    public void removerUltimaLetra() {
        if (aguardandoIniciais && iniciais.length() > 0) {
            iniciais.deleteCharAt(iniciais.length() - 1);
        }
    }

    public void confirmarOuRestart() {
        if (aguardandoIniciais) {
            if (iniciais.length() == 3) {
                // Envia para a classe de Rank de forma desacoplada!
                game.getRanking().verificarEAdicionarNovoScore(iniciais.toString().toUpperCase(), scoreFinal);
                aguardandoIniciais = false; // Muda o estado da tela para exibir os comandos normais!
            }
        } else {
            restartGame();
        }
    }

    public boolean isAguardandoIniciais() {
        return aguardandoIniciais;
    }

    public void restartGame() {
        game.setScreen(new GameScreen(game));
    }

    public void viewRanking() {
        game.setScreen(new RankScreen(game));
    }

    public void returnToMenu() {
        game.setScreen(new Menu(game));
    }

    @Override
    public void dispose() {
        font.dispose();
    }
}
