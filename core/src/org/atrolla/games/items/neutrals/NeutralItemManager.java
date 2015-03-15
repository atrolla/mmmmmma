package org.atrolla.games.items.neutrals;

import org.atrolla.games.configuration.ConfigurationConstants;
import org.atrolla.games.items.Item;

import java.util.Optional;

import static org.atrolla.games.configuration.ConfigurationConstants.ITEM_NEUTRAL_SIZE;
import static org.atrolla.games.utils.RandomUtils.getRandomCoordinates;

/**
 * Created by MicroOnde on 15/03/2015.
 */
public class NeutralItemManager {


    public Optional<Item> addItem(int time) {
        if (time % ConfigurationConstants.ITEM_NEUTRAL_SPAWN_FREQUENCY_TIME == 0) {
            return Optional.of(new Gather(getRandomCoordinates(ITEM_NEUTRAL_SIZE, ITEM_NEUTRAL_SIZE)));
        }
        return Optional.empty();
    }
}
