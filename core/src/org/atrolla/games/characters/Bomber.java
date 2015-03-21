package org.atrolla.games.characters;

import org.atrolla.games.configuration.ConfigurationConstants;
import org.atrolla.games.items.Item;
import org.atrolla.games.items.weapons.Bomb;
import org.atrolla.games.system.Player;

import java.util.Optional;

/**
 * Created by MicroOnde on 24/02/2015.
 */
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
        if (!isPlayer() || abilityIsCoolingDown(currentTime)) {
            return Optional.empty();
        }
        coolDownAbility(currentTime);
        return Optional.of(new Bomb(getCoordinates(), currentTime));
    }

    private boolean abilityIsCoolingDown(int time){
        return abilityReadyTime >= time;
    }
}
