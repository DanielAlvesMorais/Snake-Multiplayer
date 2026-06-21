package com.POO.snake;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;

/**
 * Manages the persistent top-score ranking system.
 * This class handles saving and loading player scores to and from a local JSON file,
 * keeping the data sorted and limiting the leaderboard to the top scores.
 *
 * @author Davi N. P.
 * @author Daniel A. M.
 * @author Gustavo S. L.
 * @version 1.0
 */
public class Rank {

    private ArrayList<PlayerScore> rankingList;
    private Json json;
    private String name; 

    /**
     * Initializes the ranking system by attempting to load existing scores from the disk.
     */
    public Rank() {
        this.json = new Json();
        this.rankingList = new ArrayList<>();
        loadFromFile();
    }

    /**
     * Reads the saved ranking from the local JSON file into memory.
     * Uses LibGDX's Json utility to deserialize the text directly into an ArrayList.
     */
    private void loadFromFile() {
        com.badlogic.gdx.files.FileHandle file = Gdx.files.local("ranking.json");
        if (file.exists()) {
            String text = file.readString();
            this.rankingList = json.fromJson(ArrayList.class, PlayerScore.class, text);
        }
    }

    /**
     * Evaluates a new score, adds it to the ranking list, sorts the list in descending order,
     * ensures the list does not exceed the top 5 limit, and saves the updated list to the disk.
     *
     * @param name   The string identifier/initials of the player.
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

        if (rankingList.size() > 5) {
            rankingList.remove(rankingList.size() - 1);
        }

        saveToFile();
    }

    /**
     * Retrieves the stored player name.
     *
     * @return The currently stored player name.
     */
    public String getPlayerName(){
        return this.name;
    }

    /**
     * Serializes the current ranking list to a formatted JSON string and writes it to the local file.
     */
    private void saveToFile() {
        String jsonText = json.prettyPrint(rankingList); 
        Gdx.files.local("ranking.json").writeString(jsonText, false);
    }

    /**
     * Retrieves the list of high scores.
     *
     * @return An ArrayList containing the sorted PlayerScore entries.
     */
    public ArrayList<PlayerScore> getRankingList() {
        return this.rankingList;
    }

    /**
     * Completely clears the ranking data from both memory and the local disk.
     */
    public void resetRanking() {
        this.rankingList.clear();
        saveToFile();
    }
}