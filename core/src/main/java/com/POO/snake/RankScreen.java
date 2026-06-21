package com.POO.snake;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.ScreenUtils;

/**
 * Screen implementation responsible for displaying the leaderboard.
 * Fetches the highest scores from the Rank instance and renders them to the screen.
 *
 * @author Davi N. P.
 * @author Daniel A. M.
 * @author Gustavo S. L.
 * @version 1.0
 */
public class RankScreen extends ScreenAdapter {

    private SnakeGame game;
    private BitmapFont font;
    private GlyphLayout layout;
    private KeyboardController controller;

    /**
     * Initializes the ranking screen and sets up its input processor.
     *
     * @param game The main game instance used to access shared resources like the Rank object.
     */
    public RankScreen(SnakeGame game) {
        this.game = game;
        this.font = new BitmapFont();
        this.layout = new GlyphLayout();

        this.controller = new KeyboardController(this);
        Gdx.input.setInputProcessor(controller);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0f, 0f, 0.2f, 1);

        float screenWidth = Gdx.graphics.getWidth();

        game.getBatch().begin();

        font.getData().setScale(2.5f);
        font.setColor(Color.WHITE);
        layout.setText(font, "RANKING DE PONTOS");
        font.draw(game.getBatch(), "RANKING DE PONTOS", (screenWidth - layout.width) / 2, 420);

        font.getData().setScale(1.5f);
        font.setColor(Color.YELLOW);

        ArrayList<PlayerScore> rankingList = game.getRanking().getRankingList();

        float yPosition = 340;
        if (rankingList.isEmpty()) {
            layout.setText(font, "Nenhum recorde registrado ainda!");
            font.draw(game.getBatch(), "Nenhum recorde registrado ainda!", (screenWidth - layout.width) / 2, yPosition);
        } else {
            for (int i = 0; i < Math.min(rankingList.size(), 5); i++) {
                PlayerScore player = rankingList.get(i);
                String lineText = (i + 1) + ". " + player.getName() + " - " + player.getPoints() + " pts";

                layout.setText(font, lineText);
                font.draw(game.getBatch(), lineText, (screenWidth - layout.width) / 2, yPosition);
                yPosition -= 40; 
            }
        }

        font.getData().setScale(1f);
        font.setColor(Color.LIGHT_GRAY);
        layout.setText(font, "Pressione ESC para voltar ao Menu");
        font.draw(game.getBatch(), "Pressione ESC para voltar ao Menu", (screenWidth - layout.width) / 2, 50);

        game.getBatch().end();
    }

    /**
     * Dispatches a screen change to return to the main menu.
     */
    public void backToMenu() {
        game.setScreen(new Menu(game));
    }

    @Override
    public void dispose() {
        font.dispose();
    }
}