package org.atrolla.games.characters;

import org.atrolla.games.configuration.ConfigurationConstants;
import org.atrolla.games.items.weapons.Bomb;
import org.atrolla.games.system.Player;

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
    public Bomb useAbility(int time) {
        if (!isPlayer() || abilityIsCoolingDown(time)) {
            return null;
        }
        abilityReadyTime = time + ConfigurationConstants.BOMBER_ABILITY_COOLDOWN_DURATION;
        return new Bomb(getCoordinates(), time);
    }

    private boolean abilityIsCoolingDown(int time){
        return abilityReadyTime >= time;
    }
}