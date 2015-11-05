package org.atrolla.games.characters;

import com.badlogic.gdx.math.Rectangle;
import org.atrolla.games.items.Item;
import org.atrolla.games.items.neutrals.NeutralItem;
import org.atrolla.games.system.Coordinates;
import org.atrolla.games.system.Direction;
import org.atrolla.games.system.Player;

import java.util.Optional;

import static org.atrolla.games.characters.CharacterState.*;
import static org.atrolla.games.configuration.ConfigurationConstants.*;

/**
 * Created by MicroOnde on 24/02/2015.
 */
public abstract class GameCharacter {

    private static int counter = 0;


    private final int id;
    private Player player;
    private Direction direction;
    private Coordinates coordinates;
    private CharacterState state;
    private final Rectangle hitbox;
    private Optional<NeutralItem> neutralItem;
    protected int abilityReadyTime;

    protected GameCharacter(Player player) {
        this.player = player;
        this.state = ALIVE;
        direction = Direction.DOWN;
        this.id = counter++;
        this.hitbox = new Rectangle();
        neutralItem = Optional.empty();
        abilityReadyTime = GAME_CHARACTER_INITIAL_READY_TIME;
    }

    public final void moves(Direction direction) {
        if (isAlive()) {
            if (!Direction.STOP.equals(direction)) {
                this.direction = direction;
            }
            coordinates = direction.move(coordinates);
            updateHitbox();
        }
    }

    public final void updateHitbox() {
        this.hitbox.set((float) coordinates.getX(), (float) coordinates.getY(), GAME_CHARACTER_WIDTH, GAME_CHARACTER_HEIGHT);
    }

    public final void teleports(Coordinates coordinates) {
        this.coordinates = coordinates;
        updateHitbox();
    }

    protected final void teleports(Coordinates coordinates, Direction direction) {
        this.direction = direction;
        teleports(coordinates);
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

    public void setPlayer(Player player) {
        this.player = player;
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
        state = isPlayer() ? DEAD : KNOCK_OUT;
    }

    public final void hitByMage(GameCharacter mageDisguisedClass) {
        //if it is a player and it has same class as the disguised mage class
        //or if it is a mage
        if ((isPlayer() && mageDisguisedClass.getClass().isAssignableFrom(this.getClass()))
                || Mage.class.isAssignableFrom(this.getClass())) {
            hit();
        } else {
            state = KNOCK_OUT;
        }
    }

    public final void awake() {
        state = (state == KNOCK_OUT) ? ALIVE : DEAD;
    }

    public final boolean isAlive() {
        return ALIVE.equals(state);
    }

    public final boolean isKnockOut() {
        return KNOCK_OUT.equals(state);
    }

    public abstract void coolDownAbility(int time);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameCharacter that = (GameCharacter) o;

        return id == that.id;

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
        return usedItem.map(nItem -> nItem.isUsed(time));
    }
}
