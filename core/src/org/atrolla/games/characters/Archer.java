package org.atrolla.games.characters;

import org.atrolla.games.items.Item;
import org.atrolla.games.items.weapons.Arrow;
import org.atrolla.games.system.Player;

/**
 * Created by MicroOnde on 24/02/2015.
 */
public class Archer extends GameCharacter {

    public Archer(Player player) {
        super(player);
    }

    @Override
    public Item useAbility(int time) {
        if (!isPlayer()) {
            return null;
        }
        return new Arrow(getCoordinates(), time, getDirection());
    }
}
