package org.atrolla.games.items.weapons;

import org.atrolla.games.configuration.ConfigurationConstants;
import org.atrolla.games.items.Item;
import org.atrolla.games.system.Coordinates;
import org.atrolla.games.system.Direction;

/**
 * Created by MicroOnde on 14/03/2015.
 */
public class Arrow extends Item {

    private final Direction direction;

    public Arrow(Coordinates coordinates, int time, Direction direction) {
        super(coordinates, time + ConfigurationConstants.ITEM_ARROW_RANGE_TIME_OUT);
        this.direction = direction;
    }

    @Override
    public boolean update(int timeTick) {
        moves();
        return super.update(timeTick);
    }

    private void moves() {
        setCoordinates(direction.move(getCoordinates(), ConfigurationConstants.ITEM_ARROW_MOVE_STEP));
    }

    public Direction getDirection() {
        return direction;
    }
}
