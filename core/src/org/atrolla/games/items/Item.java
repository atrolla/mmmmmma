package org.atrolla.games.items;

import org.atrolla.games.system.Coordinates;

/**
 * Created by MicroOnde on 08/03/2015.
 */
public class Item {
    private Coordinates coordinates;
    private final int timeout;

    public Item(Coordinates coordinates, int timeout) {
        this.coordinates = coordinates;
        this.timeout = timeout;
    }

    public boolean update(int timeTick) {
        //does nothing
        return isDone(timeTick);
    }

    private boolean isDone(int timeTick) {
        return timeout < timeTick;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    protected void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }
}
