package org.atrolla.games.items.weapons;

import org.atrolla.games.items.Item;
import org.atrolla.games.system.Coordinates;

import static org.atrolla.games.configuration.ConfigurationConstants.ITEM_BOMB_COUNTDOWN_DURATION;

/**
 * Created by MicroOnde on 04/03/2015.
 */
public class Bomb extends Item {

    public Bomb(Coordinates coordinates, int spawnTime) {
        super(coordinates, spawnTime + ITEM_BOMB_COUNTDOWN_DURATION);
    }
}
