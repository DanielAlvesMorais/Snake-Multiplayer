package com.POO.snake;

/**
 * Central configuration class holding all grid and screen constants for the game.
 * Replacing magic numbers with named constants ensures consistency and makes
 * adjustments (e.g., changing tile size or grid dimensions) trivial.
 *
 * @author Daniel A. M.
 * @author Davi N. P.
 * @author Gustavo S. L.
 * @version 1.0
 */
public final class GameConfig {

    /** The size in pixels of a single grid tile. */
    public static final int TILE_SIZE = 20;

    /** Number of tile columns in the playable grid. */
    public static final int GRID_COLS = 32;

    /** Number of tile rows in the playable grid (below the HUD bar). */
    public static final int GRID_ROWS = 22;

    /** Total screen width in pixels. */
    public static final int SCREEN_WIDTH = GRID_COLS * TILE_SIZE;  // 640

    /** Height of the playable area in pixels (excludes HUD). */
    public static final int PLAY_HEIGHT = GRID_ROWS * TILE_SIZE;   // 440

    /** Total screen height including the HUD bar (40 px). */
    public static final int SCREEN_HEIGHT = PLAY_HEIGHT + 40;      // 480

    /** Private constructor — this class should never be instantiated. */
    private GameConfig() {}
}
