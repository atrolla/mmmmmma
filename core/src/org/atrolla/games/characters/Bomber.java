package org.atrolla.games.characters;

import org.atrolla.games.configuration.ConfigurationConstants;
import org.atrolla.games.items.Item;
import org.atrolla.games.items.weapons.Bomb;
import org.atrolla.games.system.Player;

import java.util.Collection;
import java.util.Collections;

public class Bomber extends GameCharacter {

    public Bomber(Player player) {
        super(player);
        lives = ConfigurationConstants.BOMBER_LIVES;
    }

    @Override
    public void coolDownAbility(int time) {
        abilityReadyTime = time + ConfigurationConstants.BOMBER_ABILITY_COOLDOWN_DURATION;
    }

    @Override
    public Collection<Item> useAbility(int currentTime) {
        if (!isAlive() || !isPlayer() || abilityIsCoolingDown(currentTime)) {
            return Collections.emptySet();
        }
        coolDownAbility(currentTime);
        return Collections.singleton(new Bomb(this, currentTime));
    }

    private boolean abilityIsCoolingDown(int time){
        return abilityReadyTime >= time;
    }
}
