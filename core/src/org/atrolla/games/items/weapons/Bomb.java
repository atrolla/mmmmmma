package org.atrolla.games.items.weapons;

import com.badlogic.gdx.math.Circle;
import org.atrolla.games.characters.Bomber;
import org.atrolla.games.configuration.ConfigurationConstants;
import org.atrolla.games.items.Item;
import org.atrolla.games.system.Coordinates;

import static org.atrolla.games.configuration.ConfigurationConstants.ITEM_BOMB_COUNTDOWN_DURATION;

public class Bomb extends Item {

    private final Circle hitbox;
    private boolean explodes = false;

    public Bomb(Bomber bomber, int currentTime) {
        super(bomber.getCoordinates(), currentTime + ITEM_BOMB_COUNTDOWN_DURATION, bomber);
        final Coordinates coordinates = bomber.getCoordinates();
        this.hitbox = new Circle((float) coordinates.getX(), (float) coordinates.getY(), ConfigurationConstants.EXPLOSION_RADIUS_SIZE);
    }

    /**
     *
     * Bomb explodes when time-out is reached, then can be removed
     */
    @Override
    public boolean update(int timeTick) {
        if (explodes) {
            return true;
        }
        final boolean isDone = super.update(timeTick);
        if (isDone) { //explodes
            explodes = true;
        }
        return false;
    }

    @Override
    public Circle getHitbox() {
        return hitbox;
    }

    public boolean isExploding(){
        return explodes;
    }
}
