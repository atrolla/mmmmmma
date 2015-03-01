package org.atrolla.game.core;

import org.atrolla.game.characters.Knight;
import org.atrolla.game.configuration.ConfigurationConstants;
import org.atrolla.game.engine.Coordinates;
import org.atrolla.game.engine.Direction;
import org.atrolla.game.engine.Player;
import org.atrolla.game.input.KeyboardManager;
import org.junit.Before;
import org.junit.Test;

import static com.badlogic.gdx.Input.*;
import static org.junit.Assert.assertEquals;

public class KeyboardManagerTests {
    private KeyboardManager keyboardManager;
    private Knight knight;

    @Before
    public void setUp() throws Exception {
        keyboardManager = new KeyboardManager();
        knight = new Knight(new Player());
        knight.teleports(new Coordinates(0.0d, 0.0d));
    }

    @Test
    public void test_change_direction() throws Exception {
        knight.setDirection(Direction.DOWN);
        keyboardManager.moveCharacter(knight, Keys.UP);
        assertEquals(knight.getDirection(), Direction.UP);
    }

    @Test
    public void test_move_when_up_is_pressed_and_direction_is_also_right() throws Exception {
        knight.setDirection(Direction.UP);
        knight.teleports(new Coordinates(0.0d, 0.0d));
        keyboardManager.moveCharacter(knight, Keys.UP);
        assertEquals(knight.getCoordinates(), new Coordinates(0.0d, -ConfigurationConstants.MOVE_STEP));
    }

    @Test
    public void test_move_when_right_is_pressed_and_direction_is_also_right() throws Exception {
        knight.setDirection(Direction.RIGHT);
        knight.teleports(new Coordinates(0.0d, 0.0d));
        keyboardManager.moveCharacter(knight, Keys.RIGHT);
        assertEquals(knight.getDirection(), Direction.RIGHT);
        assertEquals(knight.getCoordinates(), new Coordinates(ConfigurationConstants.MOVE_STEP, 0.0d));
    }

    @Test
    public void test_move_when_down_is_pressed_and_direction_is_also_down() throws Exception {
        knight.setDirection(Direction.DOWN);
        knight.teleports(new Coordinates(0.0d, 0.0d));
        keyboardManager.moveCharacter(knight, Keys.DOWN);
        assertEquals(knight.getDirection(), Direction.DOWN);
        assertEquals(knight.getCoordinates(), new Coordinates(0.0d, ConfigurationConstants.MOVE_STEP));
    }

    @Test
    public void test_move_when_left_is_pressed_and_direction_is_also_left() throws Exception {
        knight.setDirection(Direction.LEFT);
        knight.teleports(new Coordinates(0.0d, 0.0d));
        keyboardManager.moveCharacter(knight, Keys.LEFT);
        assertEquals(knight.getDirection(), Direction.LEFT);
        assertEquals(knight.getCoordinates(), new Coordinates(-ConfigurationConstants.MOVE_STEP, 0.0d));
    }
}
