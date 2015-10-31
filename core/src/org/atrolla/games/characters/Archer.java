package org.atrolla.games.characters;

import org.atrolla.games.items.Item;
import org.atrolla.games.items.weapons.Arrow;
import org.atrolla.games.system.Player;

import java.util.Optional;

/**
 * Created by MicroOnde on 24/02/2015.
 */
public class Archer extends GameCharacter {

    public Archer(Player player) {
        super(player);
    }

    @Override
    public void coolDownAbility(int time) {

    }

    @Override
    public Optional<Item> useAbility(int time) {
        if (!isPlayer()) {
            return null;
        }
        return Optional.of(new Arrow(this, time));
    }
}
