package org.atrolla.games.items.neutrals;

import com.badlogic.gdx.math.Circle;
import org.atrolla.games.characters.GameCharacter;
import org.atrolla.games.configuration.ConfigurationConstants;
import org.atrolla.games.game.Round;
import org.atrolla.games.items.Item;
import org.atrolla.games.system.Coordinates;

import java.util.Optional;

/**
 *
 * NeutralItem spawns at different time in a round, their goal is to add a bonus to the player who pick the NeutralItem.<br/>
 * Once it is picked, it disappears from the screen and can be used by the player once.<br/>
 * Also replaces the item picked by the player if he has already one.
 */
public abstract class NeutralItem extends Item {

    private Optional<GameCharacter> picker;
    private int usedTime;
    private final Circle hitbox;

    public NeutralItem(Coordinates coordinates, int timeout) {
        super(coordinates, timeout, null);
        picker = Optional.empty();
        hitbox = new Circle((float) coordinates.getX(), (float) coordinates.getY(), ConfigurationConstants.ITEM_NEUTRAL_SIZE);
    }

    public NeutralItem isUsed(int time) {
        usedTime = time;
        return this;
    }

    public abstract void applyEffect(Round round);

    public boolean isPicked(GameCharacter gameCharacter) {
        gameCharacter.pick(this);
        picker = Optional.of(gameCharacter);
        return gameCharacter.isPlayer();
    }

    public Optional<GameCharacter> getPicker() {
        return picker;
    }

    protected int getUsedTime() {
        return usedTime;
    }

    @Override
    public boolean update(int timeTick) {
        return picker.isPresent();
    }

    public Circle getHitbox() {
        return hitbox;
    }
}
