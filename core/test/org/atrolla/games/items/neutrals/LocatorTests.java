package org.atrolla.games.items.neutrals;

import org.atrolla.games.characters.Archer;
import org.atrolla.games.system.Player;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static junit.framework.TestCase.assertFalse;
import static org.atrolla.games.configuration.ConfigurationConstants.NEUTRAL_ITEM_SIZE;
import static org.atrolla.games.utils.RandomUtils.getRandomCoordinates;
import static org.junit.Assert.assertTrue;

/**
 * Created by MicroOnde on 15/03/2015.
 */
public class LocatorTests {

    private Locator locator;

    @Before
    public void setUp() throws Exception {
        locator = new Locator(getRandomCoordinates(NEUTRAL_ITEM_SIZE, NEUTRAL_ITEM_SIZE), Integer.MAX_VALUE);
    }

    @Test
    public void locatorCanOnlyBePickedByAPlayer() throws Exception {
        assertFalse(locator.isPicked(new Archer(Player.BOT)));
        assertTrue(locator.isPicked(new Archer(new Player(Optional.empty(), Optional.empty()))));
    }

    @Test
    public void pickerPlayerCanUseLocator() throws Exception {
        final Archer player = new Archer(new Player(Optional.empty(), Optional.empty()));
        locator.isPicked(player);
        assertTrue(player.useNeutralItem(42).get() instanceof Locator);
    }

}
