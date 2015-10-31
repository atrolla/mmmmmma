package org.atrolla.games.items.weapons;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Shape2D;
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
        Coordinates coord = null;
        final Coordinates coordinates = character.getCoordinates();
        switch (character.getDirection()) {
            case UP:
                coord = coordinates.translateXandY(ConfigurationConstants.GAME_CHARACTER_WIDTH / 2, ConfigurationConstants.GAME_CHARACTER_HEIGHT);
                break;
            case DOWN:
                coord = coordinates.translateX(ConfigurationConstants.GAME_CHARACTER_WIDTH / 2);
                break;
            case LEFT:
                coord = coordinates.translateY(ConfigurationConstants.GAME_CHARACTER_HEIGHT / 2);
                break;
            case RIGHT:
                coord = coordinates.translateXandY(ConfigurationConstants.GAME_CHARACTER_WIDTH, ConfigurationConstants.GAME_CHARACTER_HEIGHT / 2);
                break;
            case UP_LEFT:
                coord = coordinates.translateY(ConfigurationConstants.GAME_CHARACTER_HEIGHT);
                break;
            case UP_RIGHT:
                coord = coordinates.translateXandY(ConfigurationConstants.GAME_CHARACTER_WIDTH, ConfigurationConstants.GAME_CHARACTER_HEIGHT);
                break;
            case DOWN_LEFT:
                coord = coordinates;
                break;
            case DOWN_RIGHT:
                coord = coordinates.translateX(ConfigurationConstants.GAME_CHARACTER_WIDTH);
                break;
            case STOP:
                coord = coordinates;
                break;
        }
        return new Sword(coord, time + ConfigurationConstants.ITEM_SWORD_ACTION_TIME_OUT, character);
    }

    @Override
    public Shape2D getHitbox() {
        return hitbox;
    }
}
