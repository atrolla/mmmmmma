package org.atrolla.games.characters;

import org.atrolla.games.items.Item;
import org.atrolla.games.items.weapons.Sword;
import org.atrolla.games.system.Player;

/**
 * Created by MicroOnde on 24/02/2015.
 */
public class Knight extends GameCharacter {
    public Knight(Player player) {
        super(player);
    }

    @Override
    public Item useAbility(int time) {
        if (!isPlayer()) {
            return null;
        }
        return Sword.generates(getCoordinates(), time, getDirection());
    }
}
