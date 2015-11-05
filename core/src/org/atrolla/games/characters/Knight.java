package org.atrolla.games.characters;

import org.atrolla.games.items.Item;
import org.atrolla.games.items.weapons.Sword;
import org.atrolla.games.system.Player;

import java.util.Optional;

public class Knight extends GameCharacter {
    public Knight(Player player) {
        super(player);
    }

    @Override
    public void coolDownAbility(int time) {

    }

    @Override
    public Optional<Item> useAbility(int time) {
        if (!isAlive() || !isPlayer()) {
            return Optional.empty();
        }
        return Optional.of(Sword.generates(this, time));
    }
}
