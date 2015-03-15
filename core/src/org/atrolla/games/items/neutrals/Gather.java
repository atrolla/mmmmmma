package org.atrolla.games.items.neutrals;

import org.atrolla.games.characters.GameCharacter;
import org.atrolla.games.configuration.ConfigurationConstants;
import org.atrolla.games.game.Round;
import org.atrolla.games.system.Coordinates;
import org.atrolla.games.utils.RandomUtils;

/**
 * Created by MicroOnde on 15/03/2015.
 */
public class Gather extends NeutralItem {

    public Gather(Coordinates coordinates) {
        super(coordinates, Integer.MAX_VALUE);
    }

    @Override
    public void applyEffect(Round round) {
        if (getUsedTime() > 0 && getPicker().isPresent()) {
            final Class<? extends GameCharacter> pickerClass = getPicker().get().getClass();
            final Coordinates randomCoordinates = RandomUtils.getRandomCoordinates(ConfigurationConstants.GAME_CHARACTER_WIDTH, ConfigurationConstants.GAME_CHARACTER_HEIGHT);
            round.getCharacters().stream().filter(pickerClass::isInstance).forEach(c -> c.teleports(randomCoordinates));
        }
    }

}