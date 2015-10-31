package org.atrolla.games.characters;

import org.atrolla.games.items.weapons.Arrow;
import org.atrolla.games.items.weapons.Bomb;
import org.atrolla.games.items.weapons.Sword;
import org.atrolla.games.system.Coordinates;
import org.atrolla.games.system.Player;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by MicroOnde on 22/03/2015.
 */
public class MageTests {


    private static final int ABILITY_USE_TIME = 0;
    private Mage mage;

    @Before
    public void setUp() throws Exception {
        mage = new Mage(new Player(Optional.empty(), Optional.empty()));
        mage.teleports(new Coordinates(42, 1337));
    }

    @Test
    public void mageBotCannotUseHisAbility() throws Exception {
        final Mage magebot = new Mage(Player.BOT);
        assertFalse(magebot.useAbility(ABILITY_USE_TIME).isPresent());
    }

    @Test
     public void mageTurnsIntoAnotherClass() throws Exception {
        mage.turnsInto(CharacterClasses.BOMBER);
        assertTrue(mage.getCharacterClass().isPresent());
        assertEquals(Bomber.class, mage.getCharacterClass().get());
    }

    @Test
    public void mageTurnedIntoKnightAbilityIsToUseHisSword() throws Exception {
        mage.turnsInto(CharacterClasses.KNIGHT);
        assertTrue(mage.useAbility(ABILITY_USE_TIME).get() instanceof Sword);
    }

    @Test
    public void mageTurnedIntoArcherAbilityIsToThrowArrow() throws Exception {
        mage.turnsInto(CharacterClasses.ARCHER);
        assertTrue(mage.useAbility(ABILITY_USE_TIME).get() instanceof Arrow);
    }

    @Test
    public void mageTurnedIntoBomberAbilityIsToCreateBomb() throws Exception {
        mage.turnsInto(CharacterClasses.BOMBER);
        assertTrue(mage.useAbility(ABILITY_USE_TIME).get() instanceof Bomb);
    }

    @Test
    public void mageCanOnlyKillSamePlayerClass() throws Exception {
        mage.turnsInto(CharacterClasses.BOMBER);
        assertTrue(mage.useAbility(ABILITY_USE_TIME).get() instanceof Bomb);
    }
}
