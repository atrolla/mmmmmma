package org.atrolla.games.items;

import org.atrolla.games.system.Coordinates;

/**
 * Created by MicroOnde on 08/03/2015.
 */
public class Item {
    private final Coordinates coordinates;
    private final int timeout;

    public Item(Coordinates coordinates, int timeout) {
        this.coordinates = coordinates;
        this.timeout = timeout;
    }

    public boolean checkIsDone(int timeTick) {
        return timeout < timeTick;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }
}
