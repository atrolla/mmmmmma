package org.atrolla.games.items;

import com.badlogic.gdx.math.Circle;
import org.atrolla.games.characters.GameCharacter;
import org.atrolla.games.system.Coordinates;

public abstract class Item {
    private Coordinates coordinates;
    private final int timeout;
    private final GameCharacter user;

    public Item(Coordinates coordinates, int timeout, GameCharacter user) {
        this.coordinates = coordinates;
        this.timeout = timeout;
        this.user = user;
    }

    // each item has a lifetime and if reached, return true
    public boolean update(int timeTick) {
        //does nothing
        return isDone(timeTick);
    }

    private boolean isDone(int timeTick) {
        return timeout < timeTick;
    }

    public final Coordinates getCoordinates() {
        return coordinates;
    }

    protected void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public GameCharacter getUser() {
        return user;
    }

    public abstract Circle getHitbox();
}
