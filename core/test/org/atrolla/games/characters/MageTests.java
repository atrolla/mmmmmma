package org.atrolla.games.characters;

import org.atrolla.games.items.weapons.MageSpell;
import org.atrolla.games.system.Coordinates;
import org.atrolla.games.system.Player;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

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
        assertThat(magebot.useAbility(ABILITY_USE_TIME)).isEmpty();
    }

    @Test
    public void mageAbilityIsToCreateSpells() throws Exception {
        assertThat(mage.useAbility(ABILITY_USE_TIME)).hasOnlyElementsOfType(MageSpell.class);
    }
}
