package org.atrolla.games.items.neutrals;

import org.atrolla.games.configuration.ConfigurationConstants;
import org.atrolla.games.items.Item;
import org.atrolla.games.utils.RandomUtils;

import java.util.Optional;

import static org.atrolla.games.configuration.ConfigurationConstants.NEUTRAL_ITEM_SIZE;
import static org.atrolla.games.utils.RandomUtils.getRandomCoordinates;

/**
 * Created by MicroOnde on 15/03/2015.
 */
public class NeutralItemManager {

    public Optional<Item> addItem(int time) {
        if (time > 0 && time % ConfigurationConstants.NEUTRAL_ITEM_SPAWN_FREQUENCY_TIME == 0) {
            final int i = RandomUtils.between0AndExcluded(2);
            switch (i) {
                case 0:
                    return Optional.of(new Switch(getRandomCoordinates(NEUTRAL_ITEM_SIZE, NEUTRAL_ITEM_SIZE), time + ConfigurationConstants.NEUTRAL_ITEM_DESPAWN));
                case 1:
                    return Optional.of(new Locator(getRandomCoordinates(NEUTRAL_ITEM_SIZE, NEUTRAL_ITEM_SIZE), time + ConfigurationConstants.NEUTRAL_ITEM_DESPAWN));
            }
        }
        return Optional.empty();
    }
}
