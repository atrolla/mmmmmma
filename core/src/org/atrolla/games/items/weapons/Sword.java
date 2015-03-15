package org.atrolla.games.items.weapons;

import org.atrolla.games.configuration.ConfigurationConstants;
import org.atrolla.games.items.Item;
import org.atrolla.games.system.Coordinates;
import org.atrolla.games.system.Direction;

/**
 * Created by MicroOnde on 15/03/2015.
 */
public class Sword extends Item {

    private Sword(Coordinates coordinates, int timeout) {
        super(coordinates, timeout);
    }

    public static Sword generates(final Coordinates coordinates, int abilityUsedTime, final Direction direction) {
        Coordinates coord = null;
        switch (direction) {
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
        return new Sword(coord, abilityUsedTime + ConfigurationConstants.ITEM_SWORD_ACTION_TIME_OUT);
    }
}
