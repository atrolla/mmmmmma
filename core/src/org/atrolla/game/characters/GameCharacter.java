package org.atrolla.game.characters;

import com.badlogic.gdx.math.Rectangle;
import org.atrolla.game.configuration.ConfigurationConstants;
import org.atrolla.game.items.Item;
import org.atrolla.game.system.Coordinates;
import org.atrolla.game.system.Direction;
import org.atrolla.game.system.Player;

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
    private Rectangle hitbox;

    protected GameCharacter(Player player) {
        this.player = player;
        this.state = CharacterState.ALIVE;
        direction = Direction.DOWN;
        this.id = counter++;
        this.hitbox = new Rectangle();
    }

    public void moves(Direction direction) {
        if(canMove()) {
            if (!Direction.STOP.equals(direction)) {
                this.direction = direction;
            }
            coordinates = direction.move(coordinates);
            updateHitbox();
        }
    }

    public void updateHitbox() {
        this.hitbox.set((float) coordinates.getX(), (float) coordinates.getY(),
                ConfigurationConstants.PLAYER_WIDTH, ConfigurationConstants.PLAYER_HEIGHT);
    }

    public void teleports(Coordinates coordinates) {
        this.coordinates = coordinates;
        updateHitbox();
    }

    public Direction getDirection() {
        return direction;
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

    public Rectangle getHitbox() { return hitbox; }

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

    public Item useAbility(int time) {
        return null;
    }
}
