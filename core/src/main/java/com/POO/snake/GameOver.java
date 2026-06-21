package com.POO.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.ScreenUtils;

/**
 * Screen displayed after a match concludes.
 * Shows the winner, final score, and handles the logic for capturing player initials
 * if their score qualifies for the top ranking.
 *
 * @author Davi N. P.
 * @author Daniel A. M.
 * @author Gustavo S. L.
 * @version 1.0
 */
public class GameOver extends ScreenAdapter {

    private SnakeGame game;
    private BitmapFont font;
    private GlyphLayout layout;
    private KeyboardController controller;

    private String winner;
    private int finalScore;
    private Texture headP1;
    private Texture headP2;

    private static final int SPRITE_SIZE = 80;
    private static final int MINIMUM_RANK_SCORE = 3;

    private StringBuilder initials = new StringBuilder();
    private boolean waitingInitials;

    /**
     * Initializes the Game Over screen with match statistics.
     *
     * @param game       The main game instance.
     * @param winner     A string identifier for the winner (e.g., "P1", "P2", "Empate").
     * @param finalScore The highest score achieved in the match.
     * @param headP1     Texture of player 1's head for display.
     * @param headP2     Texture of player 2's head for display.
     */
    public GameOver(SnakeGame game, String winner, int finalScore, Texture headP1, Texture headP2) {
        this.game = game;
        this.winner = winner;
        this.finalScore = finalScore;
        this.headP1 = headP1;
        this.headP2 = headP2;
        this.font = new BitmapFont();
        this.layout = new GlyphLayout();

        this.waitingInitials = !winner.equals("Empate") && finalScore >= MINIMUM_RANK_SCORE;

        this.controller = new KeyboardController(this);
        Gdx.input.setInputProcessor(controller);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0f, 0.2f, 0f, 1);
        float screenWidth = Gdx.graphics.getWidth();

        game.getBatch().begin();

        font.getData().setScale(2.5f);
        if (winner.equals("Empate")) {
            font.setColor(Color.YELLOW);
            drawCenteredText("EMPATE!", 400, screenWidth);
            game.getBatch().draw(headP1, (screenWidth / 2) - 100, 280, SPRITE_SIZE, SPRITE_SIZE);
            game.getBatch().draw(headP2, (screenWidth / 2) + 20, 280, SPRITE_SIZE, SPRITE_SIZE);
        } else {
            font.setColor(Color.GREEN);
            drawCenteredText(winner + " VENCEU!", 400, screenWidth);
            Texture winnerHead = winner.equals("P1") ? headP1 : headP2;
            game.getBatch().draw(winnerHead, (screenWidth - SPRITE_SIZE) / 2, 280, SPRITE_SIZE, SPRITE_SIZE);
        }

        font.getData().setScale(1.5f);
        font.setColor(Color.WHITE);
        drawCenteredText("Pontuacao: " + finalScore + " pts", 240, screenWidth);

        if (waitingInitials) {
            font.getData().setScale(1.2f);
            font.setColor(Color.CYAN);
            drawCenteredText("Voce entrou no Ranking! Digite 3 iniciais:", 180, screenWidth);

            String initialsText = initials.toString();
            while (initialsText.length() < 3) {
                initialsText += " _";
            }

            font.getData().setScale(2.5f);
            font.setColor(Color.WHITE);
            drawCenteredText(initialsText.toUpperCase(), 130, screenWidth);
        } else {
            font.getData().setScale(1.1f);
            font.setColor(Color.WHITE);
            drawCenteredText("Pressione ENTER para Jogar Novamente", 160, screenWidth);
            font.setColor(Color.LIGHT_GRAY);
            drawCenteredText("Pressione R para ver o Ranking", 120, screenWidth);
            drawCenteredText("Pressione ESC para fechar o jogo", 80, screenWidth);
        }

        game.getBatch().end();
    }

    private void drawCenteredText(String text, float y, float screenWidth) {
        layout.setText(font, text);
        font.draw(game.getBatch(), text, (screenWidth - layout.width) / 2, y);
    }

    /**
     * Appends a character to the player's initials if the maximum length is not yet reached.
     *
     * @param letter The character inputted by the user.
     */
    public void addLetter(char letter) {
        if (waitingInitials && initials.length() < 3) {
            initials.append(Character.toUpperCase(letter));
        }
    }

    /**
     * Deletes the last entered character from the player's initials.
     */
    public void removeLastLetter() {
        if (waitingInitials && initials.length() > 0) {
            initials.deleteCharAt(initials.length() - 1);
        }
    }

    /**
     * Confirms the entered initials and sends them to the rank system, or restarts the game
     * if the ranking phase is already completed.
     */
    public void confirmOrRestart() {
        if (waitingInitials) {
            if (initials.length() == 3) {
                game.getRanking().checkAndAddNewScore(initials.toString().toUpperCase(), finalScore);
                waitingInitials = false; 
            }
        } else {
            restartGame();
        }
    }

    /**
     * Checks if the screen is currently in the state of waiting for player initials.
     *
     * @return True if waiting for initials, false otherwise.
     */
    public boolean isWaitingForInitials() {
        return waitingInitials;
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