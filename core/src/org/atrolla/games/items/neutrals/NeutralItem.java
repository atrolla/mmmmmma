package org.atrolla.games.items.neutrals;

import com.badlogic.gdx.math.Circle;
import org.atrolla.games.characters.GameCharacter;
import org.atrolla.games.configuration.ConfigurationConstants;
import org.atrolla.games.game.GameItems;
import org.atrolla.games.game.Round;
import org.atrolla.games.items.Item;
import org.atrolla.games.system.Coordinates;

import java.util.Optional;

/**
 * NeutralItem spawns at different time in a round, their goal is to add a bonus to the player who pick the NeutralItem.<br/>
 * Once it is picked, it disappears from the screen and can be used by the player once.<br/>
 * Also replaces the item picked by the player if he has already one.
 */
public abstract class NeutralItem extends Item {

    private Optional<GameCharacter> picker;
    private int pickedTime = Integer.MAX_VALUE;
    protected int usedTime;
    private final Circle hitbox;

    public NeutralItem(Coordinates coordinates, int timeout) {
        super(coordinates, timeout, null);
        picker = Optional.empty();
        hitbox = new Circle((float) coordinates.getX(), (float) coordinates.getY(), ConfigurationConstants.NEUTRAL_ITEM_SIZE);
    }

    public NeutralItem used(int time) {
        usedTime = time;
        pickedTime = time;
        return this;
    }

    public abstract void applyEffect(Round round, GameItems gameItems);

    public boolean isPicked(GameCharacter gameCharacter) {
        gameCharacter.pick(this);
        picker = Optional.of(gameCharacter);
        return gameCharacter.isPlayer();
    }

    public Optional<GameCharacter> getPicker() {
        return picker;
    }

    public boolean isUsed() {
        return usedTime > 0;
    }

    @Override
    public boolean update(int timeTick) {
        if (pickedTime==Integer.MAX_VALUE && picker.isPresent()) {
            pickedTime = timeTick + ConfigurationConstants.NEUTRAL_ITEM_PICK_SHOW_DELAY;
        }
        return pickedTime <= timeTick || super.update(timeTick);
    }

    public Circle getHitbox() {
        return hitbox;
    }
}
