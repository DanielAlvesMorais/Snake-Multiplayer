# Multiplayer Snake Game — Project Report

## Group:

* **Daniel Alves de Morais** - 16860858
* **Davi Nascimento Pereira** - 16906368
* **Gustavo Silva Lima** - 16907230

## 1. Requirements

**Required features**

* Main menu with Play Game, view Ranking, and instructions.
* Screen state manager to transition between Menu, Gameplay, Pause, Game Over, and Ranking screens.
* Two players sharing the same screen with separate keyboard controls (Arrows for P1, WASD for P2).
* Players move continuously in a 2D grid and can change orthogonal directions.
* Collision detection: hitting the opponent or one's own body results in Game Over.
* Screen wrap-around: moving past the screen edges teleports the snake to the opposite side.
* Real-time score and elapsed time displayed on the HUD.
* Game ends immediately upon a fatal collision, declaring the winner or a tie.
* Save game state: Top 5 high scores are persisted to a local `.json` file.
* View previously saved high scores from the main menu or post-match screen.

**Additional requirements (implementation-specific)**

* A procedural visual rendering system (`SnakeRenderer`) calculates the rotation and sprite to use (head, straight body, corner curve, or tail) based on the relative position of neighboring body segments.
* Apples spawn in random grid-aligned locations, guaranteeing they never spawn inside a snake's body.
* Dynamic difficulty: snake movement speed increases slightly every time an apple is consumed.
* Input decoupled from the game loop: `KeyboardController` centralizes input handling and routes actions based on the active screen context.
* Audio is managed by a Singleton `SoundManager` that loads assets once and prevents memory leaks.
* Full Javadoc documentation generated via Gradle.

## 2. Project Description

**Screen flow**

**Plaintext**

```
MAIN_MENU
    │
    ├─ New Game ──► GAMEPLAY ──► GAME_OVER ──► RANK_SCREEN ──► MAIN_MENU
    │                   │            │           
    │                   │            └───────────► MAIN_MENU
    │                   │
    │               PAUSE_MENU ──► GAMEPLAY (resume)
    │
    ├─ Ranking ───► RANK_SCREEN
    │
    └─ Instructions ► INSTRUCTIONS_SCREEN ──► MAIN_MENU
```

`SnakeGame` (Main) owns the render loop and delegates each frame to the active `Screen`. `GameScreen` handles the gameplay loop, while other screens handle UI and state transitions.

**Entity structure**

**Plaintext**

```
SnakeGame (Main)
 ├── GameScreen
 │    ├── Snake (P1, P2)
 │    │    └── Deque<SnakeBody>
 │    ├── Apple
 │    ├── SnakeRenderer
 │    ├── HudRenderer
 │    └── KeyboardController
 ├── Rank
 │    └── ArrayList<PlayerScore>
 └── SoundManager (Singleton)
```

**Gameplay loop (per frame)**

1. Read player input (handled asynchronously by `KeyboardController`).
2. Accumulate delta time. Move each snake only when its individual `moveTimer` exceeds its `moveInterval`.
3. Check apple collisions. If true: grow snake, increment score, play sound, decrease `moveInterval` (speed up), and reposition apple.
4. Check critical collisions (snake vs. snake, snake vs. self). If true: determine winner, play crash sound, and trigger Game Over.
5. Open `SpriteBatch`. Draw background and apple.
6. Pass snakes to `SnakeRenderer` to draw bodies with calculated segment rotations.
7. Close `SpriteBatch`. Pass to `HudRenderer` to draw the top bar (scores and timer).

**Snake Movement**

The `Snake` uses a `Deque<SnakeBody>`. Movement is processed by calculating the new coordinates for the head based on the current `Direction`, applying screen wrap-around if necessary, and adding this `newHead` to the front of the queue (`addFirst`). If an apple was eaten, the tail is kept (growing the snake); otherwise, the tail is removed (`removeLast`), simulating forward movement in O(1) time complexity.

**Save / Load**

The `Rank` class uses LibGDX's `Json` utility. When a player qualifies for the top 5, `GameOver` prompts for 3 initials. `Rank.checkAndAddNewScore` updates the `ArrayList<PlayerScore>`, sorts it in descending order, trims it to a maximum of 5 entries, and serializes it to `ranking.json` via `Gdx.files.local`. Upon startup, the constructor automatically reads and parses this file.

## 3. Comments About the Code

* **Decoupled Rendering:** Rendering logic is strictly separated from business logic. `Snake.java` only knows about X and Y coordinates and has zero LibGDX imports, allowing it to be unit-tested without an OpenGL context. `SnakeRenderer.java` is responsible for interpreting those coordinates, picking the correct textures (corners, tails), and applying rotation mathematics before sending them to the GPU.
* **Centralized Input:** Instead of each screen polling for keys, `KeyboardController` extends `InputAdapter`. It receives references to the active screen entities and uses `switch` statements to route commands. This prevents input ghosting across screens. Audio feedback for direction changes is triggered here rather than inside `Snake.setDirection()`, keeping `Snake` free of audio dependencies.
* **Centralized Configuration:** `GameConfig` holds all grid and screen constants (`TILE_SIZE`, `GRID_COLS`, `GRID_ROWS`, `SCREEN_WIDTH`, `PLAY_HEIGHT`, `SCREEN_HEIGHT`). Every class that needs a dimension references these constants, eliminating magic numbers.
* **Testing with LibGDX Mocking:** Because `Snake` has no LibGDX dependencies, `SnakeTest` runs as plain JUnit 5 without any mock setup. For classes that do touch LibGDX (such as `Rank`), the JUnit setup uses Mockito to mock `Gdx.audio` and `Gdx.files`, allowing tests to run without launching an OpenGL window.

## 4. Test Plan

Tests are written in JUnit 5. Each test class focuses on one source class and exercises pure-logic methods.

**SnakeTest**

* `testSnakeCreation`: Verifies initial size (3), direction (UP), and score (0).
* `testBasicMovement`: Verifies head coordinates update correctly based on direction.
* `testValidAndInvalidDirectionChange`: Validates orthogonal turns and rejects 180-degree self-reversals.
* `testEatingAppleIncreasesScoreAndSize`: Asserts that eating prevents tail removal and increments score.
* `testSelfCollisionDetected`: Simulates overlapping body coordinates and triggers self-collision detection.
* `testNoFalseCollisionWhenFar`: Asserts no collision is reported when snakes are far apart.
* `testCrossCollisionWithOpponent`: Verifies cross-collision detection when heads overlap.
* `testMultipleDirectionChangesInOneTick`: Ensures only one direction change per tick is accepted.
* `testWrappingTopBoundary`: Verifies wrapping from the top edge to Y=0.
* `testWrappingRightBoundary`: Verifies wrapping from the right edge to X=0.

**AppleTest**

* `testAppleCoordinatesBoundsAndMultiples`: Uses `@RepeatedTest(50)` to ensure random generation always aligns with the tile grid and stays within screen bounds defined by `GameConfig`.

**KeyboardControllerTest**

* `testMenuControls`: Uses a mocked `Menu` to verify if pressing ENTER triggers `enterGame()` and R triggers `enterRanking()`. Rejects unmapped keys.

**RankTest**

* `testAddScoreMaintainsDescendingOrder`: Ensures added scores are sorted correctly.
* `testAddScoreKeepsOnlyTop5`: Adds 6 scores and verifies the lowest score is dropped and list size remains 5.

**PlayerScoreTest**

* `testFullConstructorAndGetters` / `testEmptyConstructorAndSetters`: Verifies standard POJO encapsulation.

**SnakeBodyTest**

* `testCreationAndGetters`: Verifies segment coordinate storage.

**GameScreenLogicTest**

* `testOnlySnake1Collides_P2Wins`: Only P1 collides — P2 wins.
* `testOnlySnake2Collides_P1Wins`: Only P2 collides — P1 wins.
* `testBothCollide_HigherScoreWins_P1`: Both collide, P1 has higher score — P1 wins.
* `testBothCollide_HigherScoreWins_P2`: Both collide, P2 has higher score — P2 wins.
* `testBothCollide_EqualScore_Tie`: Both collide with equal scores — Tie.
* `testNeitherCollides_Tie`: Neither collides — result is Tie.

## 5. Test Results

All tests passed with no failures or skipped cases (Gradle test run, JUnit 5).

| **Test class**           | **Tests** | **Failures** | **Skipped** | **Duration** |
| ------------------------ | --------- | ------------ | ----------- | ------------ |
| `AppleTest`              | 50        | 0            | 0           | 0.052 s      |
| `GameScreenLogicTest`    | 6         | 0            | 0           | 0.003 s      |
| `KeyboardControllerTest` | 1         | 0            | 0           | 0.038 s      |
| `PlayerScoreTest`        | 2         | 0            | 0           | 0.002 s      |
| `RankTest`               | 2         | 0            | 0           | 0.045 s      |
| `SnakeBodyTest`          | 1         | 0            | 0           | 0.001 s      |
| `SnakeTest`              | 10        | 0            | 0           | 0.018 s      |
| **Total**                | **72**    | **0**        | **0**       | **0.159 s**  |

## 6. Build Procedures

**Prerequisites**

* Java 11 or newer (JDK).
* No other tools need to be installed manually; the Gradle wrapper handles everything else.

**Clone and run**

1. Clone the repository

**Bash**

```
git clone https://github.com/DanielAlvesMorais/Snake-Multiplayer.git
cd Snake-Multiplayer
```

2. Clean previous builds and run the desktop launcher

**Bash**

```
# On Windows/Linux/Mac:
./gradlew clean lwjgl3:run

```

**Run the test suite**

**Bash**

```
# On Windows/Linux/Mac:
./gradlew core:test

```

*The HTML test report will be available at `core/build/reports/tests/test/index.html`.*

**Generate Javadoc Documentation**

To generate the full HTML documentation for the project structure:

**Bash**

```
# On Linux/Mac:
./gradlew javadoc

# On Windows:
gradlew.bat javadoc
```

*The output will be available at `core/build/docs/javadoc/index.html`.*

## 7. Problems

During the development of the project, the team faced several technical and software design challenges:

* **LibGDX SpriteBatch Optimization:** Understanding how to efficiently batch textures to the GPU. We had to ensure `batch.begin()` and `batch.end()` encapsulated all drawing commands properly across the `GameScreen`, `SnakeRenderer`, and `HudRenderer` to avoid `IllegalStateException`.
* **Procedural Snake Rendering:** Calculating the correct sprite angle for the snake's corners and tail based on the `x,y` differences of adjacent `SnakeBody` nodes was mathematically challenging. We implemented a relative direction check in `SnakeRenderer` to solve this.
* **JSON Serialization Refactoring:** When transitioning our codebase to English, renaming Java variables (e.g., `nome` to `name`) broke the existing `ranking.json` save files, throwing `SerializationException`. We learned to clear obsolete saves and handle `Unchecked Warnings` with the `@SuppressWarnings` annotation during generic deserialization.
* **State Management:** Passing input control cleanly between the Gameplay and Pause screens required careful reference management in `KeyboardController` to resume the exact same match instance instead of creating a new one.
* **Asset Lifecycle Management:** An early version of `GameOver` instantiated `GameAssets` inside `render()`, reloading all ten textures every frame. This was identified as a memory leak and corrected by moving asset loading to the constructor and disposal to `dispose()`.
* **MVC Audio Coupling:** An earlier version called `SoundManager.getInstance().playMove()` directly inside `Snake.setDirection()`, coupling the domain class to the audio framework. This was resolved by moving the audio call to `KeyboardController.changeDirection()`, keeping `Snake` as pure Java with no LibGDX dependencies.

## 8. Comments

The adoption of a strict Model-View-Controller (MVC) logic separation significantly improved the scalability of the project. By isolating the math and arrays inside `Snake.java` and keeping textures inside `GameAssets.java` and `SnakeRenderer.java`, writing Unit Tests became straightforward and completely independent of the LibGDX framework constraints.

The introduction of `GameConfig` as a central constants class eliminated all magic numbers from the codebase, making grid and screen dimensions trivially adjustable and consistent across all classes.

Furthermore, full Javadoc commenting was implemented across all classes, adhering to industry standards for software documentation and improving team collaboration.