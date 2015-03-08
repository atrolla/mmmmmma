package org.atrolla.game.core;

import org.atrolla.game.characters.Bomber;
import org.atrolla.game.configuration.ConfigurationConstants;
import org.atrolla.game.items.weapons.Bomb;
import org.atrolla.game.system.Coordinates;
import org.atrolla.game.system.Player;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by MicroOnde on 04/03/2015.
 */
public class BomberTests {

    public static final int ABILITY_USE_TIME = 0;
    private Bomber bomberPlayer;

    @Before
    public void setUp() throws Exception {
        bomberPlayer = new Bomber(new Player());
    }

    @Test
    public void bomberBotCannotUseHisAbility() throws Exception {
        Bomber bomberBot = new Bomber(Player.BOT);
        assertNull(bomberBot.useAbility(ABILITY_USE_TIME));
    }

    @Test
    public void bomberAbilityIsToCreateBomb() throws Exception {
        assertTrue(bomberPlayer.useAbility(ABILITY_USE_TIME) instanceof Bomb);
    }

    @Test
    public void bombIsCreatedAtCurrentBomberCoordinates() throws Exception {
        final Coordinates coordinates = new Coordinates(42, 1337);
        bomberPlayer.teleports(coordinates);
        final Bomb bomb = bomberPlayer.useAbility(ABILITY_USE_TIME);
        assertEquals(coordinates, bomb.getCoordinates());
    }

    @Test
    public void bomberCanOnlyPlaceOneBomb() throws Exception {
        assertTrue(bomberPlayer.useAbility(ABILITY_USE_TIME) instanceof Bomb);
        assertNull(bomberPlayer.useAbility(ABILITY_USE_TIME));
    }

    @Test
    public void bomberCanReplaceBombOnlyAfterItsCooldownTime() throws Exception {
        assertTrue(bomberPlayer.useAbility(ABILITY_USE_TIME) instanceof Bomb);
        assertNull(bomberPlayer.useAbility(ABILITY_USE_TIME + ConfigurationConstants.BOMBER_ABILITY_COOLDOWN_DURATION));
        assertTrue(bomberPlayer.useAbility(ABILITY_USE_TIME +1 + ConfigurationConstants.BOMBER_ABILITY_COOLDOWN_DURATION) instanceof Bomb);
    }
}
