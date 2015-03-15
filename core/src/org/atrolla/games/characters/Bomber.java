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

    public static final int BOMBER_INITIAL_READY_TIME = -1;
    int abilityReadyTime;

    public Bomber(Player player) {
        super(player);
        abilityReadyTime = BOMBER_INITIAL_READY_TIME;
    }

    @Override
    public Optional<Item> useAbility(int currentTime) {
        if (!isPlayer() || abilityIsCoolingDown(currentTime)) {
            return Optional.empty();
        }
        abilityReadyTime = currentTime + ConfigurationConstants.BOMBER_ABILITY_COOLDOWN_DURATION;
        return Optional.of(new Bomb(getCoordinates(), currentTime));
    }

    private boolean abilityIsCoolingDown(int time){
        return abilityReadyTime >= time;
    }
}
