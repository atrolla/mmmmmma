package org.atrolla.games.world;

import org.atrolla.games.characters.Archer;
import org.atrolla.games.system.Coordinates;
import org.atrolla.games.system.Player;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
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
        assertThat(STAGE_WIDTH).isEqualTo(stage.getWidth());
    }

    @Test
    public void stageHasHeight() throws Exception {
        Stage stage = new Stage(STAGE_WIDTH, STAGE_HEIGHT);
        assertThat(STAGE_HEIGHT).isEqualTo(stage.getHeight());
    }

    @Test
    public void isOutOfBoundCharacterIsTrue() throws Exception {
        final Archer archer = new Archer(Player.BOT);
        archer.teleports(new Coordinates(0, -1));
        assertThat(stage.isOutOfBound(archer)).isTrue();
    }

    @Test
    public void isOutOfBoundCharacterIsFalse() throws Exception {
        final Archer archer = new Archer(Player.BOT);
        archer.teleports(new Coordinates(10, 10));
        assertFalse(stage.isOutOfBound(archer));
    }
}
