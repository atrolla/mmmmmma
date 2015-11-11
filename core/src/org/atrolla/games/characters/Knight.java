package org.atrolla.games.characters;

import org.atrolla.games.configuration.ConfigurationConstants;
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
        abilityReadyTime = time + ConfigurationConstants.KNIGHT_ABILITY_COOLDOWN_DURATION;
    }

    @Override
    public Optional<Item> useAbility(int time) {
        if (!isAlive() || !isPlayer() || abilityIsCoolingDown(time)) {
            return Optional.empty();
        }
        coolDownAbility(time);
        return Optional.of(Sword.generates(this, time));
    }

    private boolean abilityIsCoolingDown(int time){
        return abilityReadyTime >= time;
    }
}
