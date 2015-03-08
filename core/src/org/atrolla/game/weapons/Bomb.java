package org.atrolla.game.weapons;

import org.atrolla.game.system.Coordinates;

/**
 * Created by MicroOnde on 04/03/2015.
 */
public class Bomb {
    private final Coordinates coordinates;

    public Bomb(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }
}
