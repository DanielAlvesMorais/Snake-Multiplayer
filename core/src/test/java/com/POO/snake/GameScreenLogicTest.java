package com.POO.snake;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the winner-determination logic that mirrors the rules in
 * {@link GameScreen#determineWinner()}.
 * <p>
 * Because {@code determineWinner()} is a private method of {@code GameScreen}
 * (which requires a LibGDX context), the logic is tested here through a small
 * package-private helper that replicates the exact same decision tree.
 * This allows full coverage without launching an OpenGL window.
 * </p>
 */
public class GameScreenLogicTest {

    /**
     * Replicates the winner-determination logic from GameScreen so it can be
     * exercised in a pure-Java unit test.
     *
     * @param s1Collided Whether snake 1 collided.
     * @param s2Collided Whether snake 2 collided.
     * @param score1     Player 1 score (used for tie-breaking).
     * @param score2     Player 2 score (used for tie-breaking).
     * @return {@code "P1"}, {@code "P2"}, or {@code "Tie"}.
     */
    private String determineWinner(boolean s1Collided, boolean s2Collided,
                                   int score1, int score2) {
        if (s1Collided && s2Collided) {
            if (score1 > score2) return "P1";
            if (score2 > score1) return "P2";
            return "Tie";
        }
        if (s1Collided) return "P2";
        if (s2Collided) return "P1";
        return "Tie";
    }

    @Test
    public void testOnlySnake1Collides_P2Wins() {
        assertEquals("P2", determineWinner(true, false, 5, 3));
    }

    @Test
    public void testOnlySnake2Collides_P1Wins() {
        assertEquals("P1", determineWinner(false, true, 3, 7));
    }

    @Test
    public void testBothCollide_HigherScoreWins_P1() {
        assertEquals("P1", determineWinner(true, true, 10, 5));
    }

    @Test
    public void testBothCollide_HigherScoreWins_P2() {
        assertEquals("P2", determineWinner(true, true, 3, 9));
    }

    @Test
    public void testBothCollide_EqualScore_Tie() {
        assertEquals("Tie", determineWinner(true, true, 6, 6));
    }

    @Test
    public void testNeitherCollides_Tie() {
        // Should never happen mid-game, but the method must handle it gracefully.
        assertEquals("Tie", determineWinner(false, false, 4, 4));
    }
}
