package org.atrolla.games.items.weapons;

import org.atrolla.games.characters.Archer;
import org.atrolla.games.configuration.ConfigurationConstants;
import org.atrolla.games.system.Coordinates;
import org.atrolla.games.system.Direction;
import org.atrolla.games.system.Player;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by MicroOnde on 14/03/2015.
 */
public class ArrowTests {

    private Arrow arrow;
    private Archer archer;

    @Before
    public void setUp() throws Exception {
        archer = new Archer(new Player());
        archer.teleports(new Coordinates(42, 1337));
        arrow = (Arrow) archer.useAbility(0).get();
    }

    @Test
    public void arrowHasSamePlayerDirection() throws Exception {
        assertEquals(archer.getDirection(), arrow.getDirection());
    }

    @Test
    public void arrowMovesStraightWhenUpdated() throws Exception {
        final Direction direction = arrow.getDirection();
        final Coordinates coordinates = arrow.getCoordinates();
        final boolean update = arrow.update(1);
        //is not done
        assertFalse(update);
        assertEquals(direction, arrow.getDirection());
        assertNotEquals(coordinates, arrow.getCoordinates());
    }

    @Test
    public void arrowHasMaxRangeDefinedByTimeOut() throws Exception {
        assertFalse(arrow.update(ConfigurationConstants.ITEM_ARROW_RANGE_TIME_OUT - 1));
        assertFalse(arrow.update(ConfigurationConstants.ITEM_ARROW_RANGE_TIME_OUT));
        assertTrue(arrow.update(ConfigurationConstants.ITEM_ARROW_RANGE_TIME_OUT + 1));
    }
}
