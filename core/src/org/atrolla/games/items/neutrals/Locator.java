package org.atrolla.games.items.neutrals;

import org.atrolla.games.configuration.ConfigurationConstants;
import org.atrolla.games.game.GameItems;
import org.atrolla.games.game.Round;
import org.atrolla.games.system.Coordinates;
import org.atrolla.games.utils.RandomUtils;

/**
 * Created by MicroOnde on 12/03/2016.
 */
public class Locator extends NeutralItem {

    public Locator(Coordinates coordinates, int timeout) {
        super(coordinates, timeout);
    }

    private Coordinates victimCoordinates;

    @Override
    public void applyEffect(Round round, GameItems gameItems) {
        if (!mustRegister() && isUsed() && getPicker().isPresent()) {
            round.getCharacters().players.stream()
                    .filter(c -> !c.equals(getPicker().get()))
                    .skip(RandomUtils.between0AndExcluded(round.getCharacters().players.size() - 1))
                    .findFirst()
                    .ifPresent(f -> victimCoordinates = f.getCenter());
            gameItems.unregisterItem(this, round.getTime());
            gameItems.registerItem(this, round.getTime() + ConfigurationConstants.NEUTRAL_ITEM_LOCATOR_DELAY_BEGIN);
        }
    }

    @Override
    public boolean update(int timeTick) {
        return super.update(timeTick);
    }

    public boolean mustRegister() {
        return victimCoordinates != null;
    }

    public Coordinates getVictimCoordinates() {
        return victimCoordinates;
    }
}
