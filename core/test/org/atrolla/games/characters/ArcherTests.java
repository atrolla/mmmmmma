package org.atrolla.games.characters;

import org.atrolla.games.items.Item;
import org.atrolla.games.items.weapons.Arrow;
import org.atrolla.games.system.Coordinates;
import org.atrolla.games.system.Player;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by MicroOnde on 14/03/2015.
 */
public class ArcherTests {

    public static final int ABILITY_USE_TIME = 0;
    private Archer archerPlayer;

    @Before
    public void setUp() throws Exception {
        this.archerPlayer = new Archer(new Player(Optional.empty(), Optional.empty()));
    }

    @Test
    public void archerBotCannotUseHisAbility() throws Exception {
        Archer archerBot = new Archer(Player.BOT);
        assertNull(archerBot.useAbility(ABILITY_USE_TIME));
    }

    @Test
    public void archerAbilityIsToThrowArrow() throws Exception {
        assertTrue(archerPlayer.useAbility(ABILITY_USE_TIME).get() instanceof Arrow);
    }

    @Test
    public void arrowSpanwsAtPlayerCoordinates() throws Exception {
        final Coordinates coordinates = new Coordinates(42, 1337);
        archerPlayer.teleports(coordinates);
        final Item arrow = archerPlayer.useAbility(ABILITY_USE_TIME).get();
        assertEquals(coordinates,arrow.getCoordinates());
    }
}
