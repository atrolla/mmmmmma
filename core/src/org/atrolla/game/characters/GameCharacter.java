package org.atrolla.game.characters;

import org.atrolla.game.engine.CharacterState;
import org.atrolla.game.engine.Coordinates;
import org.atrolla.game.engine.Direction;
import org.atrolla.game.engine.Player;

/**
 * Created by MicroOnde on 24/02/2015.
 */
public abstract class GameCharacter {

    private static int counter = 0;

    private final int id;
    private final Player player;
    private Direction direction;
    private Coordinates coordinates;
    private CharacterState state;

    protected GameCharacter(Player player) {
        this.player = player;
        this.state = CharacterState.ALIVE;
        direction = Direction.DOWN;
        this.id = counter++;
    }

    public void moves(Direction direction) {
        if(canMove()) {
            if (!Direction.STOP.equals(direction)) {
                this.direction = direction;
            }
            coordinates = direction.move(coordinates);
        }
    }

    public void teleports(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public boolean isPlayer() {
        return player != Player.BOT;
    }

    public Player getPlayer() {
        return player;
    }

    public CharacterState getState() {
        return state;
    }

    public void hit() {
        state = isPlayer() ? CharacterState.DEAD : CharacterState.KNOCK_OUT;
    }

    public void awake() {
        state = isPlayer() ? CharacterState.DEAD : CharacterState.ALIVE;
    }

    public boolean canMove() {
        return CharacterState.ALIVE.equals(state);
    }

    public boolean isKnockOut() {
        return CharacterState.KNOCK_OUT.equals(state);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameCharacter that = (GameCharacter) o;

        if (id != that.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
