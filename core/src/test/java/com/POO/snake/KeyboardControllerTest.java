package com.POO.snake;

import com.badlogic.gdx.Input.Keys;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

public class KeyboardControllerTest {

    @Test
    public void testMenuControls() {
        // We create a mock Menu using Mockito to verify if the controller calls the correct methods
        Menu mockMenu = Mockito.mock(Menu.class);
        KeyboardController controller = new KeyboardController(mockMenu);

        // Simulates the user pressing ENTER
        boolean handledEnter = controller.keyDown(Keys.ENTER);
        
        assertTrue(handledEnter, "The controller should return true for the ENTER key in the menu");
        // Verifies if the enter game function was actually triggered
        Mockito.verify(mockMenu, Mockito.times(1)).enterGame();

        // Simulates the user pressing R
        boolean handledR = controller.keyDown(Keys.R);
        
        assertTrue(handledR, "The controller should return true for the R key in the menu");
        // Verifies if the view ranking function was triggered
        Mockito.verify(mockMenu, Mockito.times(1)).enterRanking();

        // Tries an unmapped key (e.g., Space)
        boolean handledSpace = controller.keyDown(Keys.SPACE);
        assertFalse(handledSpace, "The controller should return false for unmapped keys");
    }
}