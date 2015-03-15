package org.atrolla.games.characters;

import com.badlogic.gdx.math.Rectangle;
import org.atrolla.games.configuration.ConfigurationConstants;
import org.atrolla.games.items.Item;
import org.atrolla.games.items.neutrals.NeutralItem;
import org.atrolla.games.system.Coordinates;
import org.atrolla.games.system.Direction;
import org.atrolla.games.system.Player;

import java.util.Optional;

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
    private Optional<NeutralItem> neutralItem;

    protected GameCharacter(Player player) {
        this.player = player;
        this.state = CharacterState.ALIVE;
        direction = Direction.DOWN;
        this.id = counter++;
        this.hitbox = new Rectangle();
        neutralItem = Optional.empty();
    }

    public final void moves(Direction direction) {
        if (canMove()) {
            if (!Direction.STOP.equals(direction)) {
                this.direction = direction;
            }
            coordinates = direction.move(coordinates);
            updateHitbox();
        }
    }

    public final void updateHitbox() {
        this.hitbox.set((float) coordinates.getX(), (float) coordinates.getY(),
                ConfigurationConstants.GAME_CHARACTER_WIDTH, ConfigurationConstants.GAME_CHARACTER_HEIGHT);
    }

    public final void teleports(Coordinates coordinates) {
        this.coordinates = coordinates;
        updateHitbox();
    }

    public final Direction getDirection() {
        return direction;
    }

    public final Coordinates getCoordinates() {
        return coordinates;
    }

    public final boolean isPlayer() {
        return player != Player.BOT;
    }

    public final Player getPlayer() {
        return player;
    }

    public final CharacterState getState() {
        return state;
    }

    public final Rectangle getHitbox() {
        return hitbox;
    }

    public final void hit() {
        state = isPlayer() ? CharacterState.DEAD : CharacterState.KNOCK_OUT;
    }

    public final void awake() {
        state = isPlayer() ? CharacterState.DEAD : CharacterState.ALIVE;
    }

    public final boolean canMove() {
        return CharacterState.ALIVE.equals(state);
    }

    public final boolean isKnockOut() {
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

    public Optional<Item> useAbility(int time) {
        return Optional.empty();
    }

    public void pick(NeutralItem item) {
        neutralItem = Optional.ofNullable(item);
    }

    public Optional<NeutralItem> useNeutralItem(int time) {
        Optional<NeutralItem> usedItem = neutralItem;
        neutralItem = Optional.empty();
        return usedItem.map( nItem -> nItem.isUsed(time));
    }
}
