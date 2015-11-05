package org.atrolla.games.characters;

import org.atrolla.games.configuration.ConfigurationConstants;
import org.atrolla.games.items.Item;
import org.atrolla.games.items.weapons.Bomb;
import org.atrolla.games.system.Player;

import java.util.Optional;

public class Bomber extends GameCharacter {

    public Bomber(Player player) {
        super(player);
    }

    @Override
    public void coolDownAbility(int time) {
        abilityReadyTime = time + ConfigurationConstants.BOMBER_ABILITY_COOLDOWN_DURATION;
    }

    @Override
    public Optional<Item> useAbility(int currentTime) {
        if (!isAlive() || !isPlayer() || abilityIsCoolingDown(currentTime)) {
            return Optional.empty();
        }
        coolDownAbility(currentTime);
        return Optional.of(new Bomb(this, currentTime));
    }

    private boolean abilityIsCoolingDown(int time){
        return abilityReadyTime >= time;
    }
}
