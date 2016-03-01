package org.atrolla.games.items.weapons;

import org.atrolla.games.characters.Knight;
import org.atrolla.games.configuration.ConfigurationConstants;
import org.atrolla.games.system.Coordinates;
import org.atrolla.games.system.Direction;
import org.atrolla.games.system.Player;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by MicroOnde on 15/03/2015.
 */
public class SwordTests {

    private Sword sword;
    private Knight knight;
    private int ABILITY_USE_TIME;

    @Before
    public void setUp() throws Exception {
        this.knight = new Knight(new Player(Optional.empty(), Optional.empty()));
        this.knight.teleports(new Coordinates(42,1337));
        this.sword = (Sword) knight.useAbility(0).iterator().next();
        this.ABILITY_USE_TIME = ConfigurationConstants.KNIGHT_ABILITY_COOLDOWN_DURATION+1;
    }

    @Test
    public void swordLastsFewTicksDefinedByTimeOut() throws Exception {
        assertThat(sword.update(ConfigurationConstants.ITEM_SWORD_ACTION_TIME_OUT-1)).isFalse();
        assertThat(sword.update(ConfigurationConstants.ITEM_SWORD_ACTION_TIME_OUT)).isFalse();
        assertThat(sword.update(ConfigurationConstants.ITEM_SWORD_ACTION_TIME_OUT+1));
    }

    @Test
    public void swordIsCenteredWhenWalkingToTheRight() throws Exception {
        knight.moves(0,Direction.RIGHT);
        sword = (Sword) knight.useAbility(ABILITY_USE_TIME).iterator().next();
        assertThat(knight.getCenter()).isEqualTo(sword.getCoordinates());
    }

    @Test
    public void swordIsCenteredWhenWalkingToTheLeft() throws Exception {
        knight.moves(0,Direction.LEFT);
        sword = (Sword) knight.useAbility(ABILITY_USE_TIME).iterator().next();
        assertThat(knight.getCenter()).isEqualTo(sword.getCoordinates());
    }

    @Test
    public void swordIsCenteredWhenWalkingToTheTop() throws Exception {
        knight.moves(0,Direction.UP);
        sword = (Sword) knight.useAbility(ABILITY_USE_TIME).iterator().next();
        assertThat(knight.getCenter()).isEqualTo(sword.getCoordinates());
    }

    @Test
    public void swordIsCenteredWhenWalkingDown() throws Exception {
        knight.moves(0,Direction.DOWN);
        sword = (Sword) knight.useAbility(ABILITY_USE_TIME).iterator().next();
        assertThat(knight.getCenter()).isEqualTo(sword.getCoordinates());
    }

    @Test
    public void swordIsCenteredWhenWalkingToTheUpRight() throws Exception {
        knight.moves(0,Direction.UP_RIGHT);
        sword = (Sword) knight.useAbility(ABILITY_USE_TIME).iterator().next();
        assertThat(knight.getCenter()).isEqualTo(sword.getCoordinates());
    }

    @Test
    public void swordIsCenteredWhenWalkingToTheUpLeft() throws Exception {
        knight.moves(0,Direction.UP_LEFT);
        sword = (Sword) knight.useAbility(ABILITY_USE_TIME).iterator().next();
        assertThat(knight.getCenter()).isEqualTo(sword.getCoordinates());
    }

    @Test
    public void swordIsCenteredWhenWalkingToTheDownLeft() throws Exception {
        knight.moves(0,Direction.DOWN_LEFT);
        sword = (Sword) knight.useAbility(ABILITY_USE_TIME).iterator().next();
        assertThat(knight.getCenter()).isEqualTo(sword.getCoordinates());
    }

    @Test
    public void swordIsCenteredWhenWalkingDownRight() throws Exception {
        knight.moves(0,Direction.DOWN_RIGHT);
        sword = (Sword) knight.useAbility(ABILITY_USE_TIME).iterator().next();
        assertThat(knight.getCenter()).isEqualTo(sword.getCoordinates());
    }
}
