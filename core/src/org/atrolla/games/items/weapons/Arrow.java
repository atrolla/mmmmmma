package org.atrolla.games.items.weapons;

import com.badlogic.gdx.math.Shape2D;
import org.atrolla.games.characters.Archer;
import org.atrolla.games.configuration.ConfigurationConstants;
import org.atrolla.games.items.Item;
import org.atrolla.games.system.Direction;

/**
 * Created by MicroOnde on 14/03/2015.
 */
public class Arrow extends Item {

    private final Direction direction;

    public Arrow(Archer archer, int time) {
        super(archer.getCoordinates(), time + ConfigurationConstants.ITEM_ARROW_RANGE_TIME_OUT, archer);
        this.direction = archer.getDirection();
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

    @Override
    public Shape2D getHitbox() {
        return null;
    }
}
