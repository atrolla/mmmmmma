package org.atrolla.game.characters;

import org.atrolla.game.engine.CharacterState;
import org.atrolla.game.engine.Coordinates;
import org.atrolla.game.engine.Direction;
import org.atrolla.game.engine.Player;

/**
 * Created by MicroOnde on 24/02/2015.
 */
public abstract class GameCharacter {

    private final Player player;
    private Direction direction;
    private Coordinates coordinates;
    private CharacterState state;

    protected GameCharacter(Coordinates coordinates, Player player) {
        this.coordinates = coordinates;
        this.player = player;
        this.state = CharacterState.ALIVE;
        direction = Direction.DOWN;
    }

    public void moves(Direction direction) {
        if(!Direction.STOP.equals(direction)) {
            this.direction = direction;
        }
        coordinates = direction.move(coordinates);
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

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
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
}
