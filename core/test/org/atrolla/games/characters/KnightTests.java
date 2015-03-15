package org.atrolla.games.characters;

import org.atrolla.games.items.weapons.Sword;
import org.atrolla.games.system.Coordinates;
import org.atrolla.games.system.Player;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by MicroOnde on 15/03/2015.
 */
public class KnightTests {

    private static final int ABILITY_USE_TIME = 0;
    private Knight knightPlayer;

    @Before
    public void setUp() throws Exception {
        this.knightPlayer = new Knight(new Player());
        knightPlayer.teleports(new Coordinates(42,1337));
    }

    @Test
    public void knightBotCannotUseHisAbility() throws Exception {
        Knight knightBot = new Knight(Player.BOT);
        assertFalse(knightBot.useAbility(ABILITY_USE_TIME).isPresent());
    }

    @Test
    public void knightAbilityIsToUseHisSword() throws Exception {
        assertTrue(knightPlayer.useAbility(ABILITY_USE_TIME).get() instanceof Sword);
    }
}
