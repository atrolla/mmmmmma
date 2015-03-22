package org.atrolla.games.items.weapons;

import org.atrolla.games.characters.Knight;
import org.atrolla.games.configuration.ConfigurationConstants;
import org.atrolla.games.system.Coordinates;
import org.atrolla.games.system.Direction;
import org.atrolla.games.system.Player;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Created by MicroOnde on 15/03/2015.
 */
public class SwordTests {

    private Sword sword;
    private Knight knight;

    @Before
    public void setUp() throws Exception {
        this.knight = new Knight(new Player(Optional.empty(), Optional.empty()));
        this.knight.teleports(new Coordinates(42,1337));
        this.sword = (Sword) knight.useAbility(0).get();
    }

    @Test
    public void swordLastsFewTicksDefinedByTimeOut() throws Exception {
        assertFalse(sword.update(ConfigurationConstants.ITEM_SWORD_ACTION_TIME_OUT-1));
        assertFalse(sword.update(ConfigurationConstants.ITEM_SWORD_ACTION_TIME_OUT));
        assertTrue(sword.update(ConfigurationConstants.ITEM_SWORD_ACTION_TIME_OUT+1));
    }

    @Test
    public void swordIsRightToKnightWhenWalkingToTheRight() throws Exception {
        knight.moves(Direction.RIGHT);
        sword = (Sword) knight.useAbility(0).get();
        Assert.assertTrue(knight.getCoordinates().getX() < sword.getCoordinates().getX());
        Assert.assertTrue(knight.getCoordinates().getY() < sword.getCoordinates().getY());
    }

    @Test
    public void swordIsLeftToKnightWhenWalkingToTheLeft() throws Exception {
        knight.moves(Direction.LEFT);
        sword = (Sword) knight.useAbility(0).get();
        Assert.assertTrue(knight.getCoordinates().getX() == sword.getCoordinates().getX());
        Assert.assertTrue(knight.getCoordinates().getY() < sword.getCoordinates().getY());
    }

    @Test
    public void swordIsOnTopOfKnightWhenWalkingToTheTop() throws Exception {
        knight.moves(Direction.UP);
        sword = (Sword) knight.useAbility(0).get();
        Assert.assertTrue(knight.getCoordinates().getX() < sword.getCoordinates().getX());
        Assert.assertTrue(knight.getCoordinates().getY() < sword.getCoordinates().getY());
    }

    @Test
    public void swordIsUnderKnightWhenWalkingDown() throws Exception {
        knight.moves(Direction.DOWN);
        sword = (Sword) knight.useAbility(0).get();
        Assert.assertTrue(knight.getCoordinates().getX() < sword.getCoordinates().getX());
        Assert.assertTrue(knight.getCoordinates().getY() == sword.getCoordinates().getY());
    }

    @Test
    public void swordIsUpRightToKnightWhenWalkingToTheUpRight() throws Exception {
        knight.moves(Direction.UP_RIGHT);
        sword = (Sword) knight.useAbility(0).get();
        Assert.assertTrue(knight.getCoordinates().getX() < sword.getCoordinates().getX());
        Assert.assertTrue(knight.getCoordinates().getY() < sword.getCoordinates().getY());
    }

    @Test
    public void swordIsUpLeftToKnightWhenWalkingToTheUpLeft() throws Exception {
        knight.moves(Direction.UP_LEFT);
        sword = (Sword) knight.useAbility(0).get();
        Assert.assertTrue(knight.getCoordinates().getX() == sword.getCoordinates().getX());
        Assert.assertTrue(knight.getCoordinates().getY() < sword.getCoordinates().getY());
    }

    @Test
    public void swordIsDownLeftToKnightWhenWalkingToTheDownLeft() throws Exception {
        knight.moves(Direction.DOWN_LEFT);
        sword = (Sword) knight.useAbility(0).get();
        Assert.assertTrue(knight.getCoordinates().getX() == sword.getCoordinates().getX());
        Assert.assertTrue(knight.getCoordinates().getY() == sword.getCoordinates().getY());
    }

    @Test
    public void swordIsDownRightToKnightWhenWalkingDownRight() throws Exception {
        knight.moves(Direction.DOWN_RIGHT);
        sword = (Sword) knight.useAbility(0).get();
        Assert.assertTrue(knight.getCoordinates().getX() < sword.getCoordinates().getX());
        Assert.assertTrue(knight.getCoordinates().getY() == sword.getCoordinates().getY());
    }
}
