package org.atrolla.game.core;

import org.atrolla.game.characters.Bomber;
import org.atrolla.game.system.Coordinates;
import org.atrolla.game.system.Player;
import org.atrolla.game.weapons.Bomb;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by MicroOnde on 04/03/2015.
 */
public class BomberTests {

    private Bomber bomberPlayer;

    @Before
    public void setUp() throws Exception {
        bomberPlayer = new Bomber(new Player());
    }

    @Test
    public void bomberBotCannotUseHisAbility() throws Exception {
        Bomber bomberBot = new Bomber(Player.BOT);
        assertNull(bomberBot.useAbility());
    }

    @Test
    public void bomberAbilityIsToCreateBomb() throws Exception {
        assertTrue(bomberPlayer.useAbility() instanceof Bomb);
    }

    @Test
    public void bombIsCreatedAtCurrentBomberCoordinates() throws Exception {
        final Coordinates coordinates = new Coordinates(42, 1337);
        bomberPlayer.teleports(coordinates);
        final Bomb bomb = bomberPlayer.useAbility();
        assertEquals(coordinates, bomb.getCoordinates());
    }

    @Test
    public void bomberCanOnlyPlaceOneBomb() throws Exception {
        assertTrue(bomberPlayer.useAbility() instanceof Bomb);
        assertNull(bomberPlayer.useAbility());
    }

    @Test
    public void bomberCanReplaceBombOnceHeIsReady() throws Exception {
        assertTrue(bomberPlayer.useAbility() instanceof Bomb);
        assertNull(bomberPlayer.useAbility());
        bomberPlayer.setReadyToUseAbility();
        assertTrue(bomberPlayer.useAbility() instanceof Bomb);
    }
}
