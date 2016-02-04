package org.atrolla.games.items.weapons;

import com.badlogic.gdx.math.Circle;
import org.atrolla.games.characters.GameCharacter;
import org.atrolla.games.configuration.ConfigurationConstants;
import org.atrolla.games.items.Item;
import org.atrolla.games.system.Coordinates;

/**
 * A Sword is used by a Knight class player<br/>
 * It is created in the direction the player faces and located at the Character HitBox.<br/>
 * Every Character hit by it during the time it exists is knocked out / killed.
 *
 * @see org.atrolla.games.characters.Knight
 * @see ConfigurationConstants#ITEM_SWORD_ACTION_TIME_OUT
 */
public class Sword extends Item {

    private final Circle hitbox;

    private Sword(Coordinates coordinates, int timeout, GameCharacter character) {
        super(coordinates, timeout, character);
        this.hitbox = new Circle((float) coordinates.getX(), (float) coordinates.getY(), ConfigurationConstants.SWORD_SIZE);
    }

    public static Item generates(GameCharacter character, int time) {
        return new Sword(character.getCenter(), time + ConfigurationConstants.ITEM_SWORD_ACTION_TIME_OUT, character);
    }

    @Override
    public Circle getHitbox() {
        return hitbox;
    }
}
