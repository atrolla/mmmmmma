package org.atrolla.games.characters;

import org.atrolla.games.configuration.ConfigurationConstants;
import org.atrolla.games.items.weapons.Bomb;
import org.atrolla.games.system.Coordinates;
import org.atrolla.games.system.Player;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by MicroOnde on 04/03/2015.
 */
public class BomberTests {

    public static final int ABILITY_USE_TIME = 0;
    private Bomber bomberPlayer;

    @Before
    public void setUp() throws Exception {
        bomberPlayer = new Bomber(new Player(Optional.empty(), Optional.empty()));
    }

    @Test
    public void bomberBotCannotUseHisAbility() throws Exception {
        Bomber bomberBot = new Bomber(Player.BOT);
        assertFalse(bomberBot.useAbility(ABILITY_USE_TIME).isPresent());
    }

    @Test
    public void bomberAbilityIsToCreateBomb() throws Exception {
        assertTrue(bomberPlayer.useAbility(ABILITY_USE_TIME).get() instanceof Bomb);
    }

    @Test
    public void bombIsCreatedAtCurrentBomberCoordinates() throws Exception {
        final Coordinates coordinates = new Coordinates(42, 1337);
        bomberPlayer.teleports(coordinates);
        final Bomb bomb = (Bomb) bomberPlayer.useAbility(ABILITY_USE_TIME).get();
        assertEquals(coordinates, bomb.getCoordinates());
    }

    @Test
    public void bomberCanOnlyPlaceOneBomb() throws Exception {
        assertTrue(bomberPlayer.useAbility(ABILITY_USE_TIME).get() instanceof Bomb);
        assertFalse(bomberPlayer.useAbility(ABILITY_USE_TIME).isPresent());
    }

    @Test
    public void bomberCanReplaceBombOnlyAfterItsCooldownTime() throws Exception {
        assertTrue(bomberPlayer.useAbility(ABILITY_USE_TIME).get() instanceof Bomb);
        assertFalse(bomberPlayer.useAbility(ABILITY_USE_TIME + ConfigurationConstants.BOMBER_ABILITY_COOLDOWN_DURATION).isPresent());
        assertTrue(bomberPlayer.useAbility(ABILITY_USE_TIME +1 + ConfigurationConstants.BOMBER_ABILITY_COOLDOWN_DURATION).get() instanceof Bomb);
    }
}
