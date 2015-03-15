package org.atrolla.games.items.neutrals;

import com.badlogic.gdx.math.Circle;
import org.atrolla.games.characters.GameCharacter;
import org.atrolla.games.configuration.ConfigurationConstants;
import org.atrolla.games.game.Round;
import org.atrolla.games.items.Item;
import org.atrolla.games.system.Coordinates;

import java.util.Optional;

/**
 * Created by MicroOnde on 15/03/2015.
 */
public abstract class NeutralItem extends Item {

    private Optional<GameCharacter> picker;
    private int usedTime;
    private Circle hitbox;

    public NeutralItem(Coordinates coordinates, int timeout) {
        super(coordinates, timeout);
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
