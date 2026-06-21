package com.POO.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.files.FileHandle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class RankTest {

    @BeforeEach
    public void setUp() {
        // Mocks the LibGDX file context to avoid a NullPointerException
        Gdx.files = Mockito.mock(Files.class);
        FileHandle mockFileHandle = Mockito.mock(FileHandle.class);
        
        // Tells the test to pretend that the "ranking.json" file does not exist initially
        when(Gdx.files.local(anyString())).thenReturn(mockFileHandle);
        when(mockFileHandle.exists()).thenReturn(false);
    }

    @Test
    public void testAddScoreMaintainsDescendingOrder() {
        Rank rank = new Rank();
        
        rank.checkAndAddNewScore("AAA", 10);
        rank.checkAndAddNewScore("BBB", 50);
        rank.checkAndAddNewScore("CCC", 30);

        // The list must be ordered: BBB (50), CCC (30), AAA (10)
        assertEquals("BBB", rank.getRankingList().get(0).getName());
        assertEquals("CCC", rank.getRankingList().get(1).getName());
        assertEquals("AAA", rank.getRankingList().get(2).getName());
    }

    @Test
    public void testAddScoreKeepsOnlyTop5() {
        Rank rank = new Rank();
        
        rank.checkAndAddNewScore("P01", 10);
        rank.checkAndAddNewScore("P02", 20);
        rank.checkAndAddNewScore("P03", 30);
        rank.checkAndAddNewScore("P04", 40);
        rank.checkAndAddNewScore("P05", 50);
        
        // Adds a 6th player. The lowest score (P01 with 10 points) should disappear.
        rank.checkAndAddNewScore("P06", 60);

        assertEquals(5, rank.getRankingList().size(), "The list should not have more than 5 elements");
        assertEquals("P06", rank.getRankingList().get(0).getName(), "The first place should be P06 with 60 points");
        
        // Verifies if the lowest element (10 points) was indeed removed
        boolean containsLowest = rank.getRankingList().stream()
                                     .anyMatch(p -> p.getPoints() == 10);
        assertFalse(containsLowest, "The player with 10 points should have been removed from the top 5");
    }
}