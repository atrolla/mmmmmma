package org.atrolla.game.core;

import org.atrolla.game.characters.Archer;
import org.atrolla.game.system.Coordinates;
import org.atrolla.game.system.Player;
import org.atrolla.game.world.Stage;
import org.junit.Test;

import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Created by MicroOnde on 25/02/2015.
 */
public class StageTests {

    public static final double STAGE_WIDTH = 720d;
    private static final double STAGE_HEIGHT = 1280d;
    private final Stage stage = new Stage(STAGE_WIDTH, STAGE_HEIGHT);

    @Test
    public void stageHasWidth() throws Exception {
        assertEquals(STAGE_WIDTH, stage.getWidth());
    }

    @Test
    public void stageHasHeight() throws Exception {
        Stage stage = new Stage(STAGE_WIDTH, STAGE_HEIGHT);
        assertEquals(STAGE_HEIGHT, stage.getHeight());
    }

    @Test
    public void isOutOfBoundCharacterIsTrue() throws Exception {
        final Archer archer = new Archer(Player.BOT);
        archer.teleports(new Coordinates(0, -1));
        assertTrue(stage.isOutOfBound(archer));
    }

    @Test
    public void isOutOfBoundCharacterIsFalse() throws Exception {
        final Archer archer = new Archer(Player.BOT);
        archer.teleports(new Coordinates(10, 10));
        assertFalse(stage.isOutOfBound(archer));
    }
}
