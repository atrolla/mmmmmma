package org.atrolla.games.items.weapons;

import org.atrolla.games.characters.Archer;
import org.atrolla.games.configuration.ConfigurationConstants;
import org.atrolla.games.system.Coordinates;
import org.atrolla.games.system.Direction;
import org.atrolla.games.system.Player;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by MicroOnde on 14/03/2015.
 */
public class ArrowTests {

    private Arrow arrow;
    private Archer archer;

    @Before
    public void setUp() throws Exception {
        archer = new Archer(new Player(Optional.empty(), Optional.empty()));
        archer.teleports(new Coordinates(42, 1337));
        arrow = (Arrow) archer.useAbility(0).get();
    }

    @Test
    public void arrowHasSamePlayerDirection() throws Exception {
        assertThat(archer.getDirection()).isEqualTo(arrow.getDirection());
    }

    @Test
    public void arrowMovesStraightWhenUpdated() throws Exception {
        final Direction direction = arrow.getDirection();
        final Coordinates coordinates = arrow.getCoordinates();
        final boolean update = arrow.update(1);
        //is not done
        assertThat(update).isFalse();
        assertThat(direction).isEqualTo( arrow.getDirection());
        assertNotEquals(coordinates, arrow.getCoordinates());
    }

    @Test
    public void arrowHasMaxRangeDefinedByTimeOut() throws Exception {
        assertThat(arrow.update(ConfigurationConstants.ITEM_ARROW_RANGE_TIME_OUT - 1)).isFalse();
        assertThat(arrow.update(ConfigurationConstants.ITEM_ARROW_RANGE_TIME_OUT)).isFalse();
        assertThat(arrow.update(ConfigurationConstants.ITEM_ARROW_RANGE_TIME_OUT + 1)).isTrue();
    }
}
