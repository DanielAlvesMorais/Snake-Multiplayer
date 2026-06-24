package com.POO.snake;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;

/**
 * Manages the persistent top-score ranking system.
 * This class handles saving and loading player scores to and from a local JSON
 * file, keeping the data sorted in descending order and limiting the leaderboard
 * to the top 5 entries.
 *
 * @author Daniel A. M.
 * @author Davi N. P.
 * @author Gustavo S. L.
 * @version 1.0
 */
public class Rank {

    /** Maximum number of entries kept in the leaderboard. */
    private static final int MAX_ENTRIES = 5;

    private ArrayList<PlayerScore> rankingList;
    private final Json json;

    /**
     * Initializes the ranking system by attempting to load existing scores from disk.
     */
    public Rank() {
        this.json = new Json();
        this.rankingList = new ArrayList<>();
        loadFromFile();
    }

    /**
     * Reads the saved ranking from the local JSON file into memory.
     * Uses LibGDX's {@link Json} utility to deserialize the text directly into an
     * {@link ArrayList}.
     */
    private void loadFromFile() {
        com.badlogic.gdx.files.FileHandle file = Gdx.files.local("ranking.json");
        if (file.exists()) {
            String text = file.readString();
            this.rankingList = json.fromJson(ArrayList.class, PlayerScore.class, text);
        }
    }

    /**
     * Evaluates a new score, adds it to the ranking list, sorts the list in
     * descending order, ensures the list does not exceed {@value #MAX_ENTRIES}
     * entries, and saves the updated list to disk.
     *
     * @param name   The string identifier / initials of the player.
     * @param points The final score achieved by the player.
     */
    public void checkAndAddNewScore(String name, int points) {
        rankingList.add(new PlayerScore(name, points));

        Collections.sort(rankingList, new Comparator<PlayerScore>() {
            @Override
            public int compare(PlayerScore o1, PlayerScore o2) {
                return Integer.compare(o2.getPoints(), o1.getPoints());
            }
        });

        if (rankingList.size() > MAX_ENTRIES) {
            rankingList.remove(rankingList.size() - 1);
        }

        saveToFile();
    }

    /**
     * Serializes the current ranking list to a formatted JSON string and writes it
     * to the local file.
     */
    private void saveToFile() {
        String jsonText = json.prettyPrint(rankingList);
        Gdx.files.local("ranking.json").writeString(jsonText, false);
    }

    /**
     * Retrieves the list of high scores.
     *
     * @return An {@link ArrayList} containing the sorted {@link PlayerScore} entries.
     */
    public ArrayList<PlayerScore> getRankingList() {
        return this.rankingList;
    }

    /**
     * Completely clears the ranking data from both memory and the local disk file.
     */
    public void resetRanking() {
        this.rankingList.clear();
        saveToFile();
    }
}
