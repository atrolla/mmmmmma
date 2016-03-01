package org.atrolla.games.items.weapons;

import com.badlogic.gdx.math.Circle;
import org.atrolla.games.characters.Mage;
import org.atrolla.games.items.Item;
import org.atrolla.games.system.Coordinates;

import static org.atrolla.games.configuration.ConfigurationConstants.MAGE_SPELL_SIZE;

public class MageSpell extends Item {

    private final Circle hitbox;
    private boolean triggers = false;

    public MageSpell(Coordinates coordinates, int timeout, Mage user) {
        super(coordinates, timeout, user);
        this.hitbox = new Circle((float) coordinates.getX(), (float) coordinates.getY(), MAGE_SPELL_SIZE);
    }

    /**
     *
     * Mage Spell is triggered when time-out is reached, then can be removed
     */
    @Override
    public boolean update(int timeTick) {
        if (triggers) {
            return true;
        }
        final boolean isDone = super.update(timeTick);
        if (isDone) { //triggered
            triggers = true;
        }
        return false;
    }

    @Override
    public Circle getHitbox() {
        return hitbox;
    }

    public boolean isTriggered(){
        return triggers;
    }
}
