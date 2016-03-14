package org.atrolla.games.items.weapons;

import com.badlogic.gdx.math.Circle;
import org.atrolla.games.characters.Archer;
import org.atrolla.games.configuration.ConfigurationConstants;
import org.atrolla.games.items.Item;
import org.atrolla.games.system.Coordinates;
import org.atrolla.games.system.Direction;

public class Arrow extends Item {

    private final Direction direction;
    private final Circle hitbox;

    public Arrow(Archer archer, int time) {
        super(archer.getCenter(), time + ConfigurationConstants.ARROW_RANGE_TIME_OUT, archer);
        this.direction = archer.getDirection();
        this.hitbox = new Circle((float) archer.getCoordinates().getX(), (float) archer.getCoordinates().getY(), ConfigurationConstants.ARROW_HITBOX_SIZE);
    }

    /**
     * arrow moves in the shooting direction till time-out
     */
    @Override
    public boolean update(int timeTick) {
        final Coordinates coordinates = direction.move(getCoordinates(), ConfigurationConstants.ARROW_MOVE_STEP);
        move(coordinates);
        updateHitbox(coordinates);
        return super.update(timeTick);
    }

    private void updateHitbox(Coordinates coordinates) {
        hitbox.set((float) coordinates.getX(), (float) coordinates.getY(), ConfigurationConstants.ARROW_HITBOX_SIZE);
    }

    private void move(Coordinates coordinates) {
        setCoordinates(coordinates);
    }

    public Direction getDirection() {
        return direction;
    }

    @Override
    public Circle getHitbox() {
        return hitbox;
    }
}
