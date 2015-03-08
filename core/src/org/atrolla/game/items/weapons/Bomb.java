package org.atrolla.game.items.weapons;

import org.atrolla.game.items.Item;
import org.atrolla.game.system.Coordinates;

import static org.atrolla.game.configuration.ConfigurationConstants.ITEM_BOMB_COUNTDOWN_DURATION;

/**
 * Created by MicroOnde on 04/03/2015.
 */
public class Bomb extends Item {

    public Bomb(Coordinates coordinates, int timeout) {
        super(coordinates, timeout + ITEM_BOMB_COUNTDOWN_DURATION);
    }
}
